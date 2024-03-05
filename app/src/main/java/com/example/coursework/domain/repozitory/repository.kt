package com.example.coursework.domain.repozitory

import com.example.coursework.data.TrainingDao
import com.example.coursework.data.dao
import com.example.coursework.domain.model.Day
import com.example.coursework.domain.model.Training
import com.example.coursework.presentation.screens.getCurrentDay
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@Module
class repository @Inject constructor(
    val dao: dao,
    val trainingDao: TrainingDao
) {

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