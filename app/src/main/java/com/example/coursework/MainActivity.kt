package com.example.coursework

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.model.ContextModule
import com.example.coursework.model.DaggerAppComponent
import com.example.coursework.model.SleepRequestsManager
import com.example.coursework.model.saveStepsInDatabase
import com.example.coursework.model.saveStepsInDatabase1
import com.example.coursework.model.showNotificationAlarmManager
import com.example.coursework.ui.screens.MainScreen
import com.example.coursework.ui.theme.CourseworkTheme
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    lateinit var vm: HealthViewModel
    private val sleepRequestManager by lazy {
        SleepRequestsManager(applicationContext)
    }
    lateinit var controller: LifecycleCameraController
    lateinit var camera: Camera

    @Composable
    fun MeasureHeartRate() {
        Box(contentAlignment = Alignment.Center) {
//            CameraPrewiew(controller = controller, modifier = Modifier.fillMaxSize())
            camera.measure(vm)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camera = Camera(applicationContext, this)
//        this.deleteDatabase("HealthDatabase")

        val appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
        vm = appComponent.provideHealthViewModel()

        setContent {

            CourseworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    controller = remember {
                        LifecycleCameraController(applicationContext).apply {
                            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        }
                    }

                    MainScreen(
                        viewModel = vm, context = this,
                        applicationContext = applicationContext,
                        camera = { MeasureHeartRate() }
                    )
                }
            }
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.SET_ALARM,
                Manifest.permission.FOREGROUND_SERVICE_HEALTH,
                Manifest.permission.CAMERA
            ),
            1
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "sleepRequestManager.subscribeToSleepUpdates()")
            sleepRequestManager.requestSleepUpdates()
        }


    }


    override fun onStop() {
        super.onStop()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            saveStepsInDatabase(this)
            saveStepsInDatabase1(this)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showNotificationAlarmManager(this, vm)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sleepRequestManager.unsubscribeFromSleepUpdates()
    }
}


@OptIn(ExperimentalGetImage::class)
class Camera(appContext: Context, context: Context) {
    private var text by mutableStateOf("пульс")
    private var numOf = 250
    private val heartbeat_values = Array(numOf) { 0f }
    private val heartbeat_times = Array(numOf) { System.currentTimeMillis() }
    private var heartbeat_count = 0
    private var graph = false
    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    var cameraProvider = ProcessCameraProvider.getInstance(appContext).get()
        .bindToLifecycle(context as LifecycleOwner, cameraSelector, imageAnalysis)
    val startTime = System.currentTimeMillis() + 3000

    var cameraExecutor = Executors.newSingleThreadExecutor()

    @Composable
    fun measure(viewModel: HealthViewModel) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text)
            if (graph) {
                val m = List(heartbeat_count) {
                    DataPoint(
                        (heartbeat_times[it] - heartbeat_times[0]).toFloat(),
                        ((heartbeat_values.average() - heartbeat_values[it]) * 80).toFloat()
                    )
                }
                Column {
                    LineChart(m)
                    val rr = List(heartbeat_count) {
                        DataPoint(
                            (heartbeat_times[it] - heartbeat_times[0]).toFloat(),
                            heartbeat_values[it]
                        )
                    }
                    Text(text = "${heartRateAnalysis(rr)}")
                }
            }
        }


        var heartRate = 0
        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->

            if (heartbeat_count < numOf) { //
                val image = imageProxy.image

                val buffer = image!!.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                val width = image.width
                val height = image.height
                val planes = image.planes
                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * width
                var sumRed = 0
                var sumBlue = 0
                var sumGreen = 0
                var pixelCount = 0

                buffer.rewind()
                for (row in height / 2 - height / 10 until height / 2 + height / 10) {
                    for (col in width / 2 - width / 10 until width / 2 + width / 10) {
                        val offset = row * rowStride + col * pixelStride

                        val Y = bytes[offset].toInt() and 0xFF // Получаем значение яркости пикселя
                        val U =
                            bytes[0 + (row / 2) * rowStride + (col / 2) * pixelStride].toInt() and 0xFF
                        val V =
                            bytes[0 + (row / 2) * rowStride + (col / 2) * pixelStride + 1].toInt() and 0xFF


                        sumRed += Y
                        sumBlue += V
                        sumGreen += U
                        // Пропускаем остальные компоненты пикселя
                        if (pixelStride > 1) {
                            buffer.get()
                            if (pixelStride > 2) {
                                buffer.get()
                            }
                        }

                        pixelCount++
                    }
                    buffer.position(buffer.position() + rowPadding) // Пропускаем непрочитанные байты
                }
                Log.e(
                    TAG,
                    "average  ${sumRed / pixelCount}   ${sumGreen / pixelCount}  ${sumBlue / pixelCount}"
                )

                val averageRed = sumRed.toFloat() / pixelCount
                heartbeat_values[heartbeat_count] = averageRed
                heartbeat_times[heartbeat_count] = System.currentTimeMillis()
                heartbeat_count++


                imageProxy.close()
            } else {

                val average = heartbeat_values.average()
                for (i in 1..heartbeat_count - 2) {
                    if (heartbeat_values[i - 1] < heartbeat_values[i] && heartbeat_values[i] > heartbeat_values[i + 1]) {
                        heartRate++
                    }
                }
                val t = 60 * 1000 / (heartbeat_times.last() - heartbeat_times[0])
                text = "${heartRate * t} ${(heartbeat_times.last() - heartbeat_times[0]) / 1000} "
                graph = true
                Log.e(TAG, "${imageProxy.height}")
                viewModel.heartRate = heartRate * t.toInt()
            }
        }

    }
}

@Composable
fun CameraPrewiew(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

@Composable
fun LineChart(dataPoints: List<DataPoint>) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
            .horizontalScroll(rememberScrollState())


    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .width(3000.dp)
        ) {
            val strokeWidth = 3.dp.toPx()

            dataPoints.windowed(2) { (current, next) ->
                val startX = current.x / 6
                val startY = size.height / 2 + current.y
                val endX = next.x / 6
                val endY = size.height / 2 + next.y
                drawLine(
                    Color(255, 21, 123, 240),
                    Offset(startX, startY),
                    Offset(endX, endY),
                    strokeWidth
                )
            }
        }
    }
}

data class DataPoint(val x: Float, val y: Float)


fun heartRateAnalysis(list: List<DataPoint>): Int {
    var heartRate = 0
    var increases = false
    for (i in 1..list.size - 2) {
        if (increases && list[i - 1].y > list[i].y && list[i].y > list[i + 1].y) {
            increases = false
            heartRate++
        }
        if (list[i - 1].y < list[i].y && list[i].y < list[i + 1].y) {
            increases = true
        }
    }
    return heartRate * (60000 / (list.last().x - list.first().x)).toInt()

}