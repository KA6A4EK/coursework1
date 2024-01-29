package com.example.coursework.ViewM

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.R
import com.example.coursework.model.Day
import com.example.coursework.model.Receiver
import com.example.coursework.model.Training
import com.example.coursework.model.repository
import com.example.coursework.ui.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class HealthViewModel @Inject constructor(
    private val repos: repository,
    private val context: Context,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
) :
    ViewModel() {
    private val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
    var name: String = sharedPrefs.getString("name", "User") ?: "User"
    var gender: String = sharedPrefs.getString("gender", "Male") ?: "Male"
    var userHeight: Int = sharedPrefs.getInt("userHeight", 170)
    var userWeight: Int = sharedPrefs.getInt("userWeight", 60)
    var birthdayDate: String = sharedPrefs.getString("birthdayDate", getCurrentDay()) ?: getCurrentDay()
    var days: List<Day> = runBlocking { async { repos.Init() }.await() }
    var stepsTarget: Int = sharedPrefs.getInt("stepsTarget", 10000)
    var waterTarget: Int = sharedPrefs.getInt("waterTarget", 2000)
    var cupSize: Int = sharedPrefs.getInt("cupSize", 200)
    var activityTarget: Int = sharedPrefs.getInt("activityTarget", 90)
    var eatTarget: Int = sharedPrefs.getInt("eatTarget", 2500)
    var currentDay: Day = days.find { it.date == getCurrentDay() } ?: days.last()
    var trainingActivity: List<Training> = runBlocking { async { repos.getAllActivity() }.await() }
    var weightTarget: Int = 100


    
    fun showNotification() {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        } else {
            val intent = Intent(context, Receiver::class.java).apply {
                putExtra("MESSAGE", "Clicked")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            notificationManager.notify(
                1, notificationBuilder
                    .setSmallIcon(R.drawable.glass_cup_24px)
                    .setContentTitle("Время попить воды!")
                    .setContentText("Отличный момент насладиться свежестью воды прямо сейчас!")
                    .addAction(0, "попил", pendingIntent)
                    .build()
            )
        }
    }

    fun saveHealthData() {
        sharedPrefs.edit().apply {
            putString("name", name)
            putString("gender", gender)
            putInt("userHeight", userHeight)
            putInt("userWeight", userWeight)
            putString("birthdayDate", birthdayDate)
            putInt("stepsTarget", stepsTarget)
            putInt("waterTarget", waterTarget)
            putInt("activityTarget", activityTarget)
            putInt("eatTarget", eatTarget)
            putInt("cupSize",cupSize)
            apply()
        }
    }

    fun handleViewEvent(viewEvent: HealthViewEvent) {
        when (viewEvent) {
            is HealthViewEvent.Update -> {
                viewModelScope.launch {
                    repos.Update(viewEvent.day)
                    days = days.map { day ->
                        if (day.date == viewEvent.day.date) {
                            viewEvent.day
                        } else {
                            day
                        }
                    }
                    currentDay = viewEvent.day
                }
            }

            is HealthViewEvent.InsertTraining -> {
                viewModelScope.launch {
                    repos.InsertTraining(viewEvent.training)
                    trainingActivity += viewEvent.training
                }
            }
        }
    }


}

@Module
class ProvideViewModel(private val context: Context) {
    @Provides
    fun provideHealthViewModel(
        repository: repository, notificationBuilser: NotificationCompat.Builder,
        notificationManagerCompat: NotificationManagerCompat,
    ): HealthViewModel {
        return HealthViewModel(
            repository,
            context,
            notificationBuilser,
            notificationManagerCompat,
        )
    }
}


sealed class HealthViewEvent {
    data class Update(val day: Day) : HealthViewEvent()
    data class InsertTraining(val training: Training) : HealthViewEvent()
}