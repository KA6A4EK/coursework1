package com.example.coursework.domain.model

import androidx.room.TypeConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class HeartRate(
    val value : Int,
    @Serializable(with = DateSerializer::class)
    val heartRateMeasureTime : LocalDateTime = LocalDateTime.now(),
)


class HeartRateConverter {
    @TypeConverter
    fun fromHeartRate(value: List<HeartRate>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toHeartRate(value: String): List<HeartRate> {
        return Json.decodeFromString(value)
    }
}
@Serializer(forClass = LocalDateTime::class)
object DateSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}