package com.example.coursework.domain.model

import com.example.coursework.presentation.ViewM.HealthUiState

data class UserSettings(
    var CupSize : Int = 200,
    val healthUiState : HealthUiState = HealthUiState(),
    val notificationsSettings : NotificationsSettings  =NotificationsSettings()
)