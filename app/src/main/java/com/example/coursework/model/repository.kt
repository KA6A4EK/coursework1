package com.example.coursework.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.coursework.ViewM.HealthUiState
import com.example.coursework.ui.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@Module
class repository @Inject constructor(
    val dao: dao,
    val trainingDao: TrainingDao
) {
    suspend fun test() {
        dao.Insert(Day(date = "17/01/2024"))
        dao.Insert(Day(date = "16/01/2024"))
        dao.Insert(Day(date = "15/01/2024"))
        dao.Insert(Day(date = "14/01/2024"))
        dao.Insert(Day(date = "13/01/2024"))
        dao.Insert(Day(date = "11/01/2024"))
        dao.Insert(Day(date = "09/01/2024"))
        dao.Insert(Day(date = "01/01/2024"))
        dao.Insert(Day(date = "01/01/2025"))
    }

    private val uistate = MutableStateFlow(HealthUiState())
    val _uiState = uistate.asStateFlow()

    suspend fun Update(day: Day) {
        dao.Update(day)
    }

    suspend fun Insert(day: Day) {
        dao.Insert(day)
    }

    suspend fun getAllActivity(): List<Training> {
        return trainingDao.getAllActivities()
    }

    suspend fun InsertTraining(training: Training) {
        trainingDao.Insert(training)
    }

    suspend fun Init(): List<Day> {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val days = dao.getAllDays()
        if (days.isEmpty()) {
            Insert(Day())
        } else {
            if (days.find { it.date == getCurrentDay() } == null) {
                val day = Day()
                Insert(day)
                return (days + day).sortedBy { format.parse(it.date) }.reversed()
            }
        }
        return dao.getAllDays().sortedBy { format.parse(it.date) }.reversed()
    }
}


@Module
class RepositoryModule {
    @Provides
    fun provideHealthRepository(dao: dao, trainingDao: TrainingDao): repository {
        return repository(dao = dao, trainingDao = trainingDao)
    }
}