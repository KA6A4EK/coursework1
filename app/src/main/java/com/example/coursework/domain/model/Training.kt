package com.example.coursework.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursework.presentation.screens.getCurrentDay

@Entity(tableName = "Training_Activity")
data class Training(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val date: String = getCurrentDay(),
    val duration: Int,
)