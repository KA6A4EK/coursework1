package com.example.coursework.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coursework.ViewM.HealthViewModel

@Composable
fun WaterScreen(viewModel : HealthViewModel){
    var water by remember { mutableIntStateOf(0) }
    val days = viewModel.days.map { Pair(it.date,it.water) }

    Column {
        water = lazyRowProgress(target = viewModel.waterTarget, color = Color.Blue, onClick = {}, days =days)
            
        Card (Modifier.padding(10.dp)){
            Column(modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$water", style = MaterialTheme.typography.displayMedium, color = Color.White)
                ///тут картинка стакана

            }
            
        }
    }

}