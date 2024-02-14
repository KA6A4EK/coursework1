package com.example.coursework.ViewM

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.Day
import com.example.coursework.model.StepsCounter
import com.example.coursework.model.Training
import com.example.coursework.model.repository
import com.example.coursework.ui.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject


class HealthViewModel @Inject constructor(
    private val repos: repository,
    private val context: Context
) :
    ViewModel() {
    private val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
    var name: String = sharedPrefs.getString("name", "User") ?: "User"
    var gender: String = sharedPrefs.getString("gender", "Male") ?: "Male"
    var userHeight: Int = sharedPrefs.getInt("userHeight", 170)
    var userWeight: Int = sharedPrefs.getInt("userWeight", 60)
    var birthdayDate: String =
        sharedPrefs.getString("birthdayDate", getCurrentDay()) ?: getCurrentDay()
    var days: List<Day> = runBlocking { async { repos.Init() }.await() }
    var stepsTarget: Int = sharedPrefs.getInt("stepsTarget", 10000)
    var waterTarget: Int = sharedPrefs.getInt("waterTarget", 2000)
    var cupSize: Int = sharedPrefs.getInt("cupSize", 200)
    var activityTarget: Int = sharedPrefs.getInt("activityTarget", 90)
    var eatTarget: Int = sharedPrefs.getInt("eatTarget", 2500)
    var currentDay: Day = days.find { it.date == getCurrentDay() } ?: days.last().copy(water = 0)
    var trainingActivity: List<Training> = runBlocking { async { repos.getAllActivity() }.await() }
    var weightTarget: Int = sharedPrefs.getInt("weightTarget", 60)
    val permissionForSteps = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACTIVITY_RECOGNITION
    ) != PackageManager.PERMISSION_GRANTED
    val sleepTime = sharedPrefs.getLong("sleepTime",123)

    private val uiStateSharedPref = context.getSharedPreferences("uiState", Context.MODE_PRIVATE)
    val uiState = HealthUiState(
        stepsVisible = uiStateSharedPref.getBoolean("stepsVisible", true),
        waterVisible = uiStateSharedPref.getBoolean("waterVisible", true),
        bodyCompositionVisible = uiStateSharedPref.getBoolean("bodyCompositionVisible", true),
        eatVisible = uiStateSharedPref.getBoolean("eatVisible", true),
        activityVisible = uiStateSharedPref.getBoolean("activityVisible", true),
        sleepVisible = uiStateSharedPref.getBoolean("sleepVisible", true)
    )
    val stepsCounter = StepsCounter(context)



    init {
        viewModelScope.launch {
//            sharedPrefs.edit().clear().apply()
            delay(100L)

            if (!sharedPrefs.contains("lastDaySteps")) {
                sharedPrefs.edit().putInt("lastDaySteps", stepsCounter.getSteps()).apply()
                Log.e(TAG, "lastDaySteps added")
            }
            if (!sharedPrefs.contains("lastHourSteps")) {
                sharedPrefs.edit().putInt("lastHourSteps", stepsCounter.getSteps()).apply()
                Log.e(TAG, "lastHourSteps added")

            }
            val steps = stepsCounter.getSteps()
            val lastDaySteps = sharedPrefs.getInt("lastDaySteps", 404)
            val lastHourSteps = sharedPrefs.getInt("lastHourSteps", 132)
            Log.e(TAG,currentDay.stepsAtTheDay)
            currentDay.steps = steps - lastDaySteps
            val s = currentDay.stepsAtTheDay.split(", ").toMutableList()
            s[Calendar.getInstance().time.hours] = "${s[Calendar.getInstance().time.hours].toInt()+ steps-lastHourSteps}"
            currentDay.stepsAtTheDay = s.joinToString(separator = ", ")
            sharedPrefs.edit().putInt("lastHourSteps",steps).apply()
            Log.e(TAG, "view model last day steps$lastDaySteps")
            Log.e(TAG, "viewmodel get steps ${steps}")
            Log.e(TAG, "viewmodel get last hour steps ${lastHourSteps}")
            delay(100L)
            waterFromNotifications()
            handleViewEvent(viewEvent = HealthViewEvent.Update(currentDay))
            Log.e(TAG,"${sharedPrefs.getLong("sleepTime",3600)}")
        }
    }

    fun saveUiState() {
        uiStateSharedPref.edit().apply {
            putBoolean("stepsVisible", uiState.stepsVisible)
            putBoolean("waterVisible", uiState.waterVisible)
            putBoolean("bodyCompositionVisible", uiState.bodyCompositionVisible)
            putBoolean("eatVisible", uiState.eatVisible)
            putBoolean("activityVisible", uiState.activityVisible)
            putBoolean("sleepVisible", uiState.sleepVisible)
        }.apply()
    }

    fun requestPermission(): Boolean {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1
        )
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            false
        } else {
            true
        }
    }

    fun provideSharedPreference() = sharedPrefs
    fun provideContext() = context

    fun waterFromNotifications() {
        val water = sharedPrefs.getString("countWater", "${getCurrentDay()} 0")!!.split(" ")
        sharedPrefs.edit()?.putString("countWater", "${getCurrentDay()} 0")?.apply()
        if (days.isNotEmpty()) {
            if (water[0] == getCurrentDay()) {
                currentDay.water += water[1].toInt() * cupSize
                handleViewEvent(HealthViewEvent.Update(currentDay))
            } else {
                val day = days.find { it.date == water[0] }
                if (day != null) {
                    handleViewEvent(HealthViewEvent.Update(day.copy(water = day.water + water[1].toInt() * cupSize)))
                }
            }
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
            putInt("cupSize", cupSize)
            putInt("weightTarget", weightTarget)
            apply()
        }
    }

    fun getDaysToNotification() = listOf(
        sharedPrefs.getBoolean("sunday", false),
        sharedPrefs.getBoolean("monday", false),
        sharedPrefs.getBoolean("tuesday", false),
        sharedPrefs.getBoolean("wednesday", false),
        sharedPrefs.getBoolean("thursday", false),
        sharedPrefs.getBoolean("friday", false),
        sharedPrefs.getBoolean("saturday", false),
    )

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
class ProvideViewModel() {
    @Provides
    fun provideHealthViewModel(
        context: Context,
        repository: repository,
    ): HealthViewModel {
        return HealthViewModel(
            repository,
            context
        )
    }
}


sealed class HealthViewEvent {
    data class Update(val day: Day) : HealthViewEvent()
    data class InsertTraining(val training: Training) : HealthViewEvent()
}