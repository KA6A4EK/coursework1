package com.example.coursework.domain.model

data class NotificationsSettings (
    var canSendNotifications : Boolean= false,
    val CanSendNotificationsByDay: CanSendNotificationsByDay = CanSendNotificationsByDay(),
    var notificationsPeriod:String = "02:00",
    var notificationsPeriodStart: String = "09:00",
    var notificationsPeriodEnd :String= "21:00",
)