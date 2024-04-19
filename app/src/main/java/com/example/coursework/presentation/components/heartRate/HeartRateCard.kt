package com.example.coursework.presentation.components.heartRate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HeartRateCard(value: Int, time: String) {
    Card (modifier = Modifier.padding(5.dp)){
        Row (modifier = Modifier.fillMaxWidth().padding(20.dp).heightIn(50.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$value",style = MaterialTheme.typography.displaySmall )
            Text(text = "$time",style = MaterialTheme.typography.headlineMedium,
                color = Color.Gray)
        }
    }
}