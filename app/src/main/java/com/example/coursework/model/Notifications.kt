package com.example.coursework.model

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coursework.R
import com.example.coursework.ui.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import java.util.Calendar


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

//        val message = intent?.getStringExtra("MESSAGE")
//        if (message != null) {
//            val sharedPrefs =
//                context?.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
//            sharedPrefs?.edit()?.putInt("countWater", sharedPrefs.getInt("countWater", 0))
//        }
//fun showNotification() {
//    if (ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.POST_NOTIFICATIONS
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//            1
//        )
//    } else {
//        val intent = Intent(context, Receiver::class.java).apply {
//            putExtra("MESSAGE", "Clicked")
//        }
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        notificationManager.notify(
//            1, NotificationCompat.Builder(context, "channel_id")
//                .setSmallIcon(R.drawable.glass_cup_24px)
//                .setContentTitle("Время попить воды!")
//                .setContentText("Отличный момент насладиться свежестью воды прямо сейчас!")
//                .addAction(0, "попил", pendingIntent)
//                .build()
//        )
//    }
//}


class AlarmManagerForWaterNotification(private val context: Context) {
    fun alarm() {
        val intent = Intent(context, Receiver::class.java).apply {
            putExtra("MESSAGE", "Clicked")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//        }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 15)
        calendar.set(Calendar.MINUTE, 10)
        calendar.set(Calendar.SECOND, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_HALF_HOUR,
//            pendingIntent
//        )
    }
}

