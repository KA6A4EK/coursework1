package com.example.coursework.ViewM

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.Day
import com.example.coursework.model.Training
import com.example.coursework.model.repository
import com.example.coursework.ui.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject





class HealthViewModel @Inject constructor(private val repos: repository,context: Context) : ViewModel() {
    val sharedPrefs = context.getSharedPreferences("HealthPrefs", Context.MODE_PRIVATE)
    var name: String = sharedPrefs.getString("name", "User") ?: "User"
    var gender: String = sharedPrefs.getString("gender", "Male") ?: "Male"
    var userHeight: Int = sharedPrefs.getInt("userHeight", 170)
    var userWeight: Int = sharedPrefs.getInt("userWeight", 60)
    var birthdayDate: String = sharedPrefs.getString("birthdayDate", getCurrentDay()) ?: getCurrentDay()
    var days: List<Day> = runBlocking { async { repos.Init() }.await() }
    var stepsTarget: Int = sharedPrefs.getInt("stepsTarget", 10000)
    var waterTarget: Int = sharedPrefs.getInt("waterTarget", 2000)
    var activityTarget: Int = sharedPrefs.getInt("activityTarget", 90)
    var eatTarget: Int = sharedPrefs.getInt("eatTarget", 2500)
    var currentDay: Day = days.find { it.date == getCurrentDay() } ?: days.last()
    var trainingActivity: List<Training> = runBlocking { async { repos.getAllActivity() }.await() }


    fun saveHealthData(){
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
    fun provideHealthViewModel(repository: repository): HealthViewModel {
        return HealthViewModel(repository ,context)
    }
}


sealed class HealthViewEvent {
    data class Update(val day: Day) : HealthViewEvent()
    data class InsertTraining(val training: Training) : HealthViewEvent()
}