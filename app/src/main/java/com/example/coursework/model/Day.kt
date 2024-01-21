package com.example.coursework.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursework.ui.screens.getCurrentDay
import java.time.Duration

@Entity(tableName = "Health")
data class Day(
    @PrimaryKey(autoGenerate = true) val id  :Int = 0,
    val date: String = getCurrentDay(),
    var steps: Int = 0,
    var water: Int = 0,
    val eat: Int = 0,
    var activity : Int =0,
    val bmi :Int = 0
)

@Entity(tableName = "Training_Activity")
data class Training(
    @PrimaryKey(autoGenerate = true) val id  :Int = 0,
    val title : String ="",
    val date: String = getCurrentDay(),
    val duration: Int
)