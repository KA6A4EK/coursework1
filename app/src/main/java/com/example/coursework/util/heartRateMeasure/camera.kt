package com.example.coursework.util.heartRateMeasure

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import com.example.coursework.presentation.ViewM.HealthViewModel
import java.util.concurrent.Executors


@OptIn(ExperimentalGetImage::class)
class Camera(appContext: Context, context: Context) {
    private var text by mutableStateOf("пульс")
    private var numOf = 250
    private var heartbeat_values = Array(numOf) { 0f }
    private val heartbeat_times = Array(numOf) { System.currentTimeMillis() }
    private var heartbeat_count = 0
    private var graph = false
    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    var cameraProvider = ProcessCameraProvider.getInstance(appContext).get()
        .bindToLifecycle(context as LifecycleOwner, cameraSelector, imageAnalysis)
    val startTime = System.currentTimeMillis()

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
                    Text(text = "${heartRateAnalysis(rr)}", modifier = Modifier.clickable {
                        cameraExecutor.shutdownNow()
                        imageAnalysis.clearAnalyzer()
                    })
                }
            }
        }


        var heartRate = 0
        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->

            if (heartbeat_count < numOf) { //
                if (startTime + 3000 < System.currentTimeMillis()) {
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
                    Log.e(ContentValues.TAG, "average  ${sumRed / pixelCount.toFloat()}  ")

                    val averageRed = sumRed.toFloat() / pixelCount
                    heartbeat_values[heartbeat_count] = averageRed
                    heartbeat_times[heartbeat_count] = System.currentTimeMillis()
                    heartbeat_count++


                    imageProxy.close()
                }
            } else {

                for (i in 40..heartbeat_count - 2 - 15) {
                    if (heartbeat_values[i - 1] < heartbeat_values[i] && heartbeat_values[i] > heartbeat_values[i + 1]) {
                        heartRate++
                    }
                    if (heartbeat_values[i - 1] < heartbeat_values[i] && heartbeat_values[i] > heartbeat_values[i + 1]) {
                        heartRate++
                    }
                }
                val t = 60 * 1000 / (heartbeat_times[numOf - 15] - heartbeat_times[40])
                text =
                    "${heartRate * t} ${(heartbeat_times[numOf - 15] - heartbeat_times[40]) / 1000} ${(heartbeat_times[numOf - 15] - heartbeat_times[40]) / 195}"
                graph = true
                Log.e(ContentValues.TAG, "${imageProxy.height}")
                viewModel.heartRate = heartRate * t.toInt()
            }
        }

    }
}