package com.example.coursework.presentation.ViewM

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.data.local.HealthManager
import com.example.coursework.domain.model.Day
import com.example.coursework.domain.repository.repository
import com.example.coursework.presentation.screens.getCurrentDay
import com.example.coursework.util.StepsCounter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthViewModel @Inject constructor(
    private val repos: repository,
    private val context: Context,
    private val healthManager: HealthManager,
) :
    ViewModel() {

    val user = mutableStateOf(runBlocking { healthManager.readUser() })
    var days: List<Day> = runBlocking { async { repos.Init() }.await() }
    var currentDay: Day = days.find { it.date == getCurrentDay() } ?: days.last().copy(water = 0)
    val permissionForSteps = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACTIVITY_RECOGNITION
    ) != PackageManager.PERMISSION_GRANTED
    val _scanEnabled = mutableStateOf(false)

    val stepsCounter = StepsCounter(context)

    val heartRateValues = MutableStateFlow(mutableListOf<Float>())
    val liveData: StateFlow<MutableList<Float>> = heartRateValues

    init {
        viewModelScope.launch {
            val steps = stepsCounter.getSteps()
            val lastHourSteps = healthManager.readLastHourSteps() ?: steps

            val s = currentDay.stepsAtTheDay.toMutableList()
            if (lastHourSteps < steps) {
                s[Calendar.getInstance().time.hours] =
                    s[Calendar.getInstance().time.hours] + steps - lastHourSteps
                currentDay.stepsAtTheDay = s.toList()
            }
            handleViewEvent(viewEvent = HealthViewEvent.Update(currentDay))

            waterFromNotifications()
            delay(300L)
            healthManager.saveLastHourSteps(steps)

        }
    }

    fun saveUser() = viewModelScope.launch { healthManager.saveUser(user.value) }
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

    fun waterFromNotifications() {
        viewModelScope.launch {
            val water = healthManager.readCountWater().split(" ")
            if (days.isNotEmpty()) {
                if (water[0] == getCurrentDay()) {
                    currentDay.water += water[1].toInt() * user.value.userSettings.CupSize
                    handleViewEvent(HealthViewEvent.Update(currentDay))
                } else {
                    val day = days.find { it.date == water[0] }
                    if (day != null) {
                        handleViewEvent(HealthViewEvent.Update(day.copy(water = day.water + water[1].toInt() * user.value.userSettings.CupSize)))
                    }
                }
            }
        }
    }

    fun getDaysToNotificationList() = listOf(
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.sunday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.monday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.tuesday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.wednesday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.thursday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.friday,
        user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.saturday,
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
        }
    }
}

@Module
class ProvideViewModel() {
    @Provides
    fun provideHealthViewModel(
        context: Context,
        repository: repository,
        healthManager: HealthManager,

        ): HealthViewModel {
        return HealthViewModel(
            repository,
            context,
            healthManager,
        )
    }
}

sealed class HealthViewEvent {
    data class Update(val day: Day) : HealthViewEvent()
}