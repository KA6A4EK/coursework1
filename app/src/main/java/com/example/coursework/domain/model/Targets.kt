package com.example.coursework.domain.model

import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Targets(
    @SerialName ("steps") var steps: Int = 10000,
    @SerialName ("eat") val  eat: Int = 2500,
    @SerialName ("water") var water: Int = 2000,
    @SerialName ("activity") var activity: Int = 90,
    @SerialName ("sleep") val  sleep: Int = 8 * 60,
    @SerialName ("weight") var weight: Int = 60,
)

class TargetsConverter {

    @TypeConverter
    fun fromTargets(targets: Targets): String {
        return Json.encodeToString(targets)
    }

    @TypeConverter
    fun toTargets(targetsString: String): Targets {
        return Json.decodeFromString(targetsString)
    }
}