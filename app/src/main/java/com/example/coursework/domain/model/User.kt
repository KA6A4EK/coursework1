package com.example.coursework.domain.model

import com.example.coursework.presentation.screens.getCurrentDay

data class User(
    var name : String = "User",
    var gender: String ="Male",
    var birthdayDate :String = getCurrentDay(),
    val userSettings : UserSettings = UserSettings()
)
