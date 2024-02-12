package com.example.coursework.model

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.ui.screens.getCurrentDay
import com.example.coursework.ui.screens.parceTime
import dagger.Module
import dagger.Provides
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date


@Module
class NotificationModule(private val context: Context) {
    @Provides
    fun provideNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "chanel_id")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @Provides
    fun provideNotificationManager(): NotificationManagerCompat {
        val notificationManager =
            NotificationManagerCompat.from(context)
        val chanel = NotificationChannel(
            "chanel_id",
            "chanelName",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(chanel)
        return notificationManager
    }
}

class Receiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        } else {
            val notificationManager =
                NotificationManagerCompat.from(context)
            val chanel = NotificationChannel(
                "chanel_id",
                "chanelName",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(chanel)
            val notificationIntent = Intent(context, Receiver::class.java).apply {
                action = "Clicked"
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            notificationManager.notify(
                1, NotificationCompat.Builder(context!!, "chanel_id")
                    .setSmallIcon(R.drawable.glass_cup_24px)
                    .setContentTitle("Время попить воды!")
                    .setContentText("Отличный момент насладиться свежестью воды прямо сейчас!")
                    .addAction(0, "попил", pendingIntent)
                    .build()
            )
            if (intent?.action == "Clicked") {
                val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
                val water = sharedPrefs.getString("countWater", "${getCurrentDay()} 0")!!.split(" ")
                if (water[0] == getCurrentDay()) {
                    sharedPrefs.edit()
                        ?.putString("countWater", "${getCurrentDay()} ${water[1].toInt() + 1}")
                        ?.apply()
                } else {
                    sharedPrefs.edit()
                        ?.putString("countWater", "${getCurrentDay()} ${1}")?.apply()
                }
                notificationManager.cancel(1)
            }

        }
    }
}


fun showNotificationAlarmManager(context: Context,viewModel: HealthViewModel){
    val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
    val showNotifications = sharedPrefs.getBoolean("showNotifications", false)
    val days = viewModel.getDaysToNotification()
    Log.e(TAG,"LocalDate.now().dayOfWeek.value ${LocalDate.now().dayOfWeek.value }")
    Log.e(TAG,"LocalDate.now().dayOfWeek.value ${days}")
    Log.e(TAG,"showNotifications ${showNotifications}")
    Log.e(TAG,"showNotifications ${showNotifications && days[LocalDate.now().dayOfWeek.value ]}")
    if (showNotifications && days[LocalDate.now().dayOfWeek.value ]) {
        val startTime = parceTime(sharedPrefs.getString("startTime", "09:00")!!)
        val endTime = parceTime(sharedPrefs.getString("endTime", "21:00")!!)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, Receiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        val period = parceTime(sharedPrefs.getString("NotificationPeriod", "02:00")!!)
        val interval =
            (period.hour * 60 + period.minute) * 60 * 1000 // 90 минут в миллисекундах
        val currentTime = Calendar.getInstance()
        val localTime = LocalTime.now()
        val triggerTime: Long

        if (localTime < startTime) {
            Log.e(ContentValues.TAG, "first if")
            // Если текущее время раньше начала периода, устанавливаем срабатывание на 09:10
            currentTime.set(Calendar.HOUR_OF_DAY, startTime.hour)
            currentTime.set(Calendar.MINUTE, startTime.minute)
            triggerTime = currentTime.timeInMillis
        } else if (localTime.plusMinutes((period.minute + period.hour * 60).toLong()) in startTime..endTime) {
            Log.e(ContentValues.TAG, "second if")


            // Если текущее время находится в пределах периода, устанавливаем срабатывание на 2 часа от текущего времени
            currentTime.add(Calendar.HOUR_OF_DAY, period.hour)
            currentTime.add(Calendar.MINUTE, period.minute)
            triggerTime = currentTime.timeInMillis

        } else {
            Log.e(ContentValues.TAG, "else")
            // Если текущее время позже конца периода, устанавливаем срабатывание на следующий день в 09:10
            currentTime.add(Calendar.DAY_OF_MONTH, 1)
            currentTime.set(Calendar.HOUR_OF_DAY, startTime.hour)
            currentTime.set(Calendar.MINUTE, startTime.minute)
            triggerTime = currentTime.timeInMillis
        }
        Log.e(ContentValues.TAG, Date(triggerTime).toString())

        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            interval.toLong(),
            pendingIntent
        )
        Log.e(TAG,"triggerTime ${currentTime.time}")
    }
}