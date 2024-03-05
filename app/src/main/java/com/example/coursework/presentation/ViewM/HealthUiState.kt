package com.example.coursework.presentation.ViewM


data class HealthUiState(
    var edit: Boolean = false,
    var stepsVisible: Boolean,
    var waterVisible: Boolean,
    var bodyCompositionVisible: Boolean,
    var eatVisible: Boolean,
    var activityVisible: Boolean,
    var sleepVisible: Boolean,
)