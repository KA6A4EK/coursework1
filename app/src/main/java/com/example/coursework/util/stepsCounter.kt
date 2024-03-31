package com.example.coursework.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.coursework.AppContainer.ContextModule
import com.example.coursework.AppContainer.DaggerAppComponent
import com.example.coursework.R
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar

class StepsCounter(context: Context) : SensorEventListener {
    private var sensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private var stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private var step = 0

    init {
        sensorManager.registerListener(
            this,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            step = sensorEvent.values[0].toInt()
            Log.e(TAG, "onSensorChanged $step")
        }
    }

    fun getSteps() = step
}

fun saveStepsInDatabase1(context: Context) {
    Log.e(TAG, "saveStepsInDatabase")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, StepCounterService1::class.java)
    val pendingIntent =
        PendingIntent.getService(context, 123, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
    val currentTime = Calendar.getInstance()
    currentTime.set(Calendar.MINUTE, 55)
    currentTime.set(Calendar.SECOND, 0)


// Установка повтора на каждый час
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        currentTime.timeInMillis,
        AlarmManager.INTERVAL_HOUR,
        pendingIntent
    )
    Log.e(TAG, "${currentTime.time}")
}

class StepCounterService1 : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val CHANNEL_ID = "StepCounterChannel2"
        const val NOTIFICATION_ID = 113
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!
        createNotificationChannel()
        sharedPreferences = getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
        Log.e(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification().build())
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Log.e(TAG, "onStartCommand")
        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        Log.e(TAG, "onDestroy")

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val stepCount = event.values?.get(0)?.toInt()!!
            val repository = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .build().provideHealthRepository()
            val days = runBlocking { async { repository.Init() }.await() }
            var currentDay = days.find { it.date == getCurrentDay() }!!
            var hour = Calendar.getInstance().time.hours
            if (Calendar.getInstance().time.minutes < 54) {
                if (Calendar.getInstance().time.hours == 0) {
                    currentDay = days.find { it.date == getYesterdayDate() }!!
                    hour = 23

                } else {
                    hour -= 1
                }
            }

            val lastHourSteps = sharedPreferences.getInt("lastHourSteps", stepCount)
            val stepsAtTheDay = currentDay.stepsAtTheDay.toMutableList()
            stepsAtTheDay[hour] =
                if (stepCount >= lastHourSteps) (stepCount - lastHourSteps) else 0
            runBlocking {
                repository.Update(
                    currentDay.copy(
                        stepsAtTheDay = stepsAtTheDay.toList()
                    )
                )
            }
            sharedPreferences.edit().putInt("lastHourSteps", stepCount).apply()
            Log.e(TAG, "lastHourSteps $stepCount")
        }
        stopSelf()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Counter Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): NotificationCompat.Builder {
        val intent = Intent(this, Receiver::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Step Counter Service")
            .setContentText("Foreground service collecting step data")
            .setSmallIcon(R.drawable.sprint_24px)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
}

fun getYesterdayDate() =
    SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().add(Calendar.DATE, -1))

