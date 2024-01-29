package com.example.coursework.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coursework.ViewM.HealthViewEvent
import com.example.coursework.ViewM.HealthViewModel
import dagger.Module
import dagger.Provides
import java.time.Duration
import javax.inject.Inject


@Module
class NotificationModule(private val context: Context) {
    @Provides
    fun provideNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "chanel_id")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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
        val message = intent?.getStringExtra("MESSAGE")
        if (message != null) {
            val sharedPrefs =
                context?.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putInt("countWater", sharedPrefs.getInt("countWater",0) )
        }
    }

}