package com.example.coursework.model

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SleepCounter(context: Context) : SensorEventListener {
    var sleepTime : Long = 0
    var lastMovementTime: Long = 0
    var currentTime: Long = System.currentTimeMillis()
    private var sensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private var accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    init {
        sensorManager.registerListener(
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    if (isInMotion(x, y, z)) {
                        sleepTime = maxOf(sleepTime, currentTime-  lastMovementTime)
//                        Log.e(TAG,"sleepTime${sleepTime}")
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Не требуется для данной реализации.
                }
            },
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            lastMovementTime = sensorEvent.timestamp

        }
    }

    fun isInMotion(x: Float, y: Float, z: Float): Boolean {
        // Рассчитываем модуль вектора ускорения
        val accelerationMagnitude = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        // Задаем пороговое значение для среднеквадратичного ускорения,
        // которое считаем достаточным для определения движения.
        val threshold = 10f
        if (accelerationMagnitude > threshold) {
            lastMovementTime = currentTime
            currentTime = System.currentTimeMillis()
        }
        return accelerationMagnitude > threshold
    }


}
