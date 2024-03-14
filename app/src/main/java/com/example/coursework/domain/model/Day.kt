package com.example.coursework.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Health")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String = getCurrentDay(),
    var steps: Int = 0,
    var water: Int = 0,
    val eat: Int = 0,
    var activity: Int = 0,
    val bmi: Int = 0,
    val weight: Int = 60,
    val height: Int = 170,
    val sleepTime: Int = 0,
    val sleepPeriod: String = "",
    val targets: Targets = Targets(),
    var trainings: List<Training> = listOf(),
    var stepsAtTheDay: String = "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0",
)

