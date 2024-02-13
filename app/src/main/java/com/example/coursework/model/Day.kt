package com.example.coursework.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursework.ui.screens.getCurrentDay

@Entity(tableName = "Health")
data class Day(
    @PrimaryKey(autoGenerate = true) val id  :Int = 0,
    val date: String = getCurrentDay(),
    var steps: Int = 0,
    var water: Int = 0,
    val eat: Int = 0,
    var activity : Int =0,
    val bmi :Int = 0,
    val x :Int =1,//удалит
//    val stepsTarget :Int = 10000,
//    val eatTarget :Int = 2500,
//    val waterTarget :Int = 2000,
//    val activityTarget :Int = 90,
    var stepsAtTheDay : String = "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"
)

@Entity(tableName = "Training_Activity")
data class Training(
    @PrimaryKey(autoGenerate = true) val id  :Int = 0,
    val title : String ="",
    val date: String = getCurrentDay(),
    val duration: Int
)