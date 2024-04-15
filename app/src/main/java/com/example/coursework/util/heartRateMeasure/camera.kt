package com.example.coursework.util.heartRateMeasure

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerScreen(
    scanEnabled: State<Boolean>,
    onMeasured: (Int) -> Unit,
    drawGraph:(Float,Int)->Unit,
) {
    val numOf = 250
//    val heartbeat_values by remember { mutableStateOf(Array(numOf) { 0f }) }
    val heartbeat_values = remember { mutableStateOf(mutableListOf( 0f )) }

    val heartbeat_times = Array(numOf) { System.currentTimeMillis() }
    var heartbeat_count = 0

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->

            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )


    val preview = Preview.Builder().build()
    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
    var camera by remember { mutableStateOf<Camera?>(null) }

    imageAnalysis.setAnalyzer(
        Dispatchers.Default.asExecutor(),
    ) { imageProxy ->

        if (heartbeat_count < numOf) { //
            Log.e(TAG, heartbeat_count.toString())
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

            var pixelCount = 0

            buffer.rewind()
            for (row in height / 2 - height / 10 until height / 2 + height / 10) {
                for (col in width / 2 - width / 10 until width / 2 + width / 10) {
                    val offset = row * rowStride + col * pixelStride

                    val Y =
                        bytes[offset].toInt() and 0xFF // Получаем значение яркости пикселя

                    sumRed += Y
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

            val averageRed = sumRed.toFloat() / pixelCount
            heartbeat_values.value.add( averageRed)
            heartbeat_times[heartbeat_count] = System.currentTimeMillis()
            heartbeat_count++
            imageProxy.close()
            drawGraph(averageRed,heartbeat_count)

        } else {
            onMeasured(heartRateAnalysis(List(heartbeat_count) {
                DataPoint(
                    (heartbeat_times[it] - heartbeat_times[0]).toFloat(),
                    heartbeat_values.value[it]
                )
            }))
            heartbeat_count = 0
//            imageAnalysis.clearAnalyzer()
        }
    }

    val cameraProvider = ProcessCameraProvider.getInstance(context)
    LaunchedEffect(scanEnabled.value) {
        withContext(Dispatchers.IO) {
            cameraProvider.get()
        }.unbindAll()
        camera = if (scanEnabled.value) {
            withContext(Dispatchers.IO) {
                cameraProvider.get()
            }.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        } else {
            withContext(Dispatchers.IO) {
                cameraProvider.get()
            }.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        }
    }
}


//@Singleton
//@OptIn(ExperimentalGetImage::class)
//class Camera(appContext: Context, context: Context) {
//    private var text by mutableStateOf("пульс")
//    private var numOf = 250
//    private var heartbeat_values = Array(numOf) { 0f }
//    private val heartbeat_times = Array(numOf) { System.currentTimeMillis() }
//    private var heartbeat_count = 0
//    private val imageAnalysis = ImageAnalysis.Builder()
//        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//        .build()
//
//    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//    var cameraProvider = ProcessCameraProvider.getInstance(appContext).get()
//        .bindToLifecycle(context as LifecycleOwner, cameraSelector, imageAnalysis)
//
//    var cameraExecutor = Executors.newSingleThreadExecutor()
//    fun measure(viewModel: HealthViewModel) {
//        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
//            if (heartbeat_count < numOf) { //
//                Log.e(ContentValues.TAG, "${heartbeat_count}")
//                val image = imageProxy.image
//                val buffer = image!!.planes[0].buffer
//                val bytes = ByteArray(buffer.remaining())
//                buffer.get(bytes)
//                val width = image.width
//                val height = image.height
//                val planes = image.planes
//                val pixelStride = planes[0].pixelStride
//                val rowStride = planes[0].rowStride
//                val rowPadding = rowStride - pixelStride * width
//                var sumRed = 0
//                var pixelCount = 0
//                buffer.rewind()
//                for (row in height / 2 - height / 10 until height / 2 + height / 10) {
//                    for (col in width / 2 - width / 10 until width / 2 + width / 10) {
//                        val offset = row * rowStride + col * pixelStride
//                        val Y =
//                            bytes[offset].toInt() and 0xFF // Получаем значение яркости пикселя
//                        sumRed += Y
//                        if (pixelStride > 1) {
//                            buffer.get()
//                            if (pixelStride > 2) {
//                                buffer.get()
//                            }
//                        }
//                        pixelCount++
//                    }
//                    buffer.position(buffer.position() + rowPadding) // Пропускаем непрочитанные байты
//                }
//                val averageRed = sumRed.toFloat() / pixelCount
//                heartbeat_values[heartbeat_count] = averageRed
//                heartbeat_times[heartbeat_count] = System.currentTimeMillis()
//                heartbeat_count++
//                imageProxy.close()
//
//            } else {
//                val rr = List(heartbeat_count) {
//                    DataPoint(
//                        (heartbeat_times[it] - heartbeat_times[0]).toFloat(),
//                        heartbeat_values[it]
//                    )
//                }
//                Log.e(ContentValues.TAG, "viewModel.currentDay.heartRateList+=HeartRate(heartRateAnalysis(rr))")
//                Log.e(ContentValues.TAG, "${rr}")
//                Log.e(ContentValues.TAG, "${heartRateAnalysis(rr)}")
//                cameraExecutor.shutdownNow()
//                imageAnalysis.clearAnalyzer()
//                viewModel.currentDay.heartRateList += HeartRate(heartRateAnalysis(rr))
//                viewModel.heartRateValues = List(heartbeat_count) {
//                    DataPoint(
//                        (heartbeat_times[it] - heartbeat_times[0]).toFloat(),
//                        ((heartbeat_values.average() - heartbeat_values[it]) * 80).toFloat()
//                    )
//                }
//                heartbeat_count = 0
//            }
//        }
//
//    }
//}
