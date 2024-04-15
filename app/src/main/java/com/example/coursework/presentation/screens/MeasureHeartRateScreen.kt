package com.example.coursework.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.coursework.domain.model.HeartRate
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.util.heartRateMeasure.LineChart
import com.example.coursework.util.heartRateMeasure.ScannerScreen

@Composable
fun MeasureHeartRateScreen(viewModel: HealthViewModel) {
    var dataPoints by remember { mutableStateOf(emptyList<Float>()) }

    ScannerScreen(
        viewModel._scanEnabled,
        drawGraph = { value, index ->

        },
        onMeasured = {
            viewModel.currentDay.heartRateList += HeartRate(it)
            viewModel._scanEnabled.value = false
        }
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LineChart(dataPoint =0f  , i =1 )

        Button(onClick = {
            viewModel._scanEnabled.value = true
        }) {

        }
        Text(text = "${viewModel.currentDay.heartRateList.lastOrNull()?.value ?: "Нет значения"}")
        if (viewModel.currentDay.heartRateList.isNotEmpty()) {
            Text(text = "${viewModel.currentDay.heartRateList.lastOrNull()?.heartRateMeasureTime ?: "Нет данных"}")
        }
    }
}
