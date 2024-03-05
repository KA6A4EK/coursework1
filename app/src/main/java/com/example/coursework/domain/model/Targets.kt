package com.example.coursework.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Targets(
    val steps: Int = 10000,
    val eat: Int = 2500,
    val water: Int = 2000,
    val activity: Int = 90,
    val sleep: Int = 8 * 60,
    val weight: Int = 60,
)
