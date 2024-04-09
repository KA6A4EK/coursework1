package com.example.coursework.presentation.ViewM


data class HealthUiState(
    var edit: Boolean = false,
    var stepsVisible: Boolean =true,
    var waterVisible: Boolean=true,
    var bodyCompositionVisible: Boolean=true,
    var eatVisible: Boolean=true,
    var activityVisible: Boolean=true,
    var sleepVisible: Boolean=true,
)