package com.example.coursework.domain.model

import androidx.room.TypeConverter
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Training(
    val title: String = "",
    val date: String = getCurrentDay(),
    val duration: Int,
)

class TrainingListConverter {
    @TypeConverter
    fun fromTargets(training: List<Training>): String {
        return Json.encodeToString(training)
    }

    @TypeConverter
    fun toTargets(trainingString: String): List<Training> {
        return Json.decodeFromString(trainingString)
    }
}