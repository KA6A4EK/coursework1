package com.example.coursework.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity(tableName = "Health")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String = getCurrentDay(),
    var water: Int = 0,
    val eat: Int = 0,
    var activity: Int = 0,
    val bmi: Int = 0,
    var weight: Int = 60,
    var height: Int = 170,
    val sleepTime: Int = 0,
    val sleepPeriod: String = "",
    var targets: Targets = Targets(),
    var trainings: List<Training> = listOf(),
    @SerialName("heartRateList") var heartRateList: List<HeartRate> = listOf(),
    @SerialName("stepsAtTheDay") var stepsAtTheDay: List<Int> = List(24) { 0 },
){
    val steps: Int
        get(
        ) {
            return stepsAtTheDay.sum()
        }

}


class StepsAtTheDayConverter {

    @TypeConverter
    fun fromTargets(stepsAtTheDay: List<Int>): String {
        return Json.encodeToString(stepsAtTheDay)
    }

    @TypeConverter
    fun toTargets(stepsAtTheDayString: String): List<Int> {
        return Json.decodeFromString(stepsAtTheDayString)
    }
}