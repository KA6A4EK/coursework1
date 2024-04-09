package com.example.coursework.domain.model

data class CanSendNotificationsByDay (
    var monday : Boolean = false,
    var tuesday : Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var sunday: Boolean = false,
)