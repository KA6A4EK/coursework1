package com.example.coursework.ViewM

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


data class HealthUiState(
    var edit: Boolean = false,
    var stepsVisible: Boolean,
    var waterVisible: Boolean,
    var bodyCompositionVisible: Boolean,
    var eatVisible: Boolean,
    var activityVisible: Boolean,
)