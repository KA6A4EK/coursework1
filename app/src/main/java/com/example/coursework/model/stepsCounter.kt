package com.example.coursework.model

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.coursework.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class StepsCounter(context: Context) : SensorEventListener {
    private var sensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private var stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    var step = 0
//    private val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
//    val currentTime = Calendar.getInstance().time.hours
        init {
        sensorManager.registerListener(
            this,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    //            if (currentTime > 10) {
//                val sharedPrefs = getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
//                sharedPrefs.edit().putInt("lastDaySteps", step).apply()
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
             step = sensorEvent.values[0].toInt()
            Log.e(TAG, "onSensorChanged lastDaySteps")
        }
    }
    fun getSteps() =step
}

class StepCounterService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var step = 0
    override fun onCreate() {
        super.onCreate()
        val notification: Notification = createNotification()

        startForeground(NOTIFICATION_ID,notification)
        Log.e(TAG,"StepCounterService create")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        sensorManager.unregisterListener(this)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Метод, вызываемый при изменении точности датчика
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            step = event.values[0].toInt()
            Log.d(TAG, "Steps updated: $step")
        }
    }
    private fun createNotification(): Notification {
        // Создание и настройка уведомления
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running in the background")
            .setSmallIcon(R.drawable.sprint_24px)
        // Добавление других настроек уведомления
        // ...

        return builder.build()
    }
    companion object {
        private const val NOTIFICATION_ID = 12 // Уникальный идентификатор уведомления
        private const val CHANNEL_ID = "ForegroundServiceChannel" // Идентификатор канала уведомлений
    }

    fun getSteps ()= step
}
fun saveStepsInDatabase(context: Context) {
    Log.e(TAG, "saveStepsInDatabase")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, StepsCounter::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(context, 123, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
    val currentTime = Calendar.getInstance()

//    currentTime.set(Calendar.HOUR_OF_DAY, 23)
//    currentTime.set(Calendar.MINUTE, 59)
//    currentTime.add(Calendar.MINUTE, 1)
    currentTime.add(Calendar.SECOND, 10)
    val interval =
        (1) * 60 * 1000
// Установка повтора на каждый день
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        currentTime.timeInMillis,
        interval.toLong(),
        pendingIntent
    )
    Log.e(TAG, "${currentTime.time}")
}


class StepsCounterReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        val service = Intent(context!!,StepCounterService::class.java)
//        context.startForegroundService(service)

        val appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(context!!))
            .build()
        val vm = appComponent.provideHealthViewModel()
        val sharedPrefs = vm.provideSharedPreference()
        val stepsCounter = vm.stepsCounter

        Log.e(TAG, "StepsCounterReceiver")
        Log.e(TAG, "${vm.currentDay.steps}")
        Log.e(TAG, "${stepsCounter.getSteps()}")

        vm.viewModelScope.launch {
            delay(10000L)
            val steps = stepsCounter.getSteps()
            sharedPrefs.edit().putInt("lastDaySteps", steps).apply()
            Log.e(TAG, "$steps steps")
            Log.e(TAG, "${vm.currentDay.steps}")

        }
    }

}

fun schedulePeriodicWork(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val periodicWorkRequest = PeriodicWorkRequest.Builder(
        StepsCounterWorker::class.java,
        15, // интервал выполнения задачи в минутах
        TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueue(periodicWorkRequest)
}

class StepsCounterWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        var step = 0

        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Метод, вызываемый при изменении точности датчика
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                    step = event.values[0].toInt()
                    Log.d(TAG, "Steps updated: $step")
                }
            }
        }, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        return Result.success()
    }
}