package com.example.coursework

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.ViewM.ProvideViewModel
import com.example.coursework.model.ContextModule
import com.example.coursework.model.DaggerAppComponent
import com.example.coursework.model.DaoModule
import com.example.coursework.model.Receiver
import com.example.coursework.ui.screens.MainScreen
import com.example.coursework.ui.screens.parceTime
import com.example.coursework.ui.theme.CourseworkTheme
import kotlinx.coroutines.CoroutineScope
import java.time.LocalTime
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    lateinit var vm :HealthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build()
        vm = appComponent.provideHealthViewModel()
        setContent {
            CourseworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = vm)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPrefs = this.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
        val showNotifications = sharedPrefs.getBoolean("showNotifications", false)
        val days = vm.getDaysToNotification()
        Log.e(TAG,showNotifications.toString())
        Log.e(TAG,days[Calendar.DAY_OF_WEEK - 2].toString())
        Log.e(TAG,Calendar.DAY_OF_WEEK.toString())
        Log.e(TAG,days.toString())
        if (showNotifications && days[Calendar.DAY_OF_WEEK - 2]) {
            val startTime = parceTime(sharedPrefs.getString("startTime", "09:00")!!)
            val endTime = parceTime(sharedPrefs.getString("endTime", "21:00")!!)
            val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(this, Receiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
            val period = parceTime(sharedPrefs.getString("NotificationPeriod", "02:00")!!)
            val interval = (period.hour * 60 + period.minute) * 60 * 1000 // 90 минут в миллисекундах
            val currentTime = Calendar.getInstance()
            val localTime = LocalTime.now()
            val triggerTime: Long

            if (localTime < startTime) {
                Log.e(TAG, "first if")
                // Если текущее время раньше начала периода, устанавливаем срабатывание на 09:10
                currentTime.set(Calendar.HOUR_OF_DAY, startTime.hour)
                currentTime.set(Calendar.MINUTE, startTime.minute)
                triggerTime = currentTime.timeInMillis
            } else if (localTime.plusMinutes((period.minute + period.hour * 60).toLong()) in startTime..endTime) {
                Log.e(TAG, "second if")
                Log.e(TAG, startTime.toString())
                Log.e(TAG, endTime.toString())
                Log.e(TAG, "second if")
                Log.e(TAG, localTime.plusMinutes((period.minute + period.hour * 60).toLong()).toString())

                // Если текущее время находится в пределах периода, устанавливаем срабатывание на 2 часа от текущего времени
                currentTime.add(Calendar.HOUR_OF_DAY, period.hour)
                currentTime.add(Calendar.MINUTE, period.minute)
                triggerTime = currentTime.timeInMillis

            } else {
                Log.e(TAG, "else")
                // Если текущее время позже конца периода, устанавливаем срабатывание на следующий день в 09:10
                currentTime.add(Calendar.DAY_OF_MONTH, 1)
                currentTime.set(Calendar.HOUR_OF_DAY, startTime.hour)
                currentTime.set(Calendar.MINUTE, startTime.minute)
                triggerTime = currentTime.timeInMillis
            }
            Log.e(TAG,Date(triggerTime).toString())

            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                interval.toLong(),
                pendingIntent
            )
        }
    }
}


