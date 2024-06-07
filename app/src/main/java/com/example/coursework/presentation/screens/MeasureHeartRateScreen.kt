package com.example.coursework.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.coursework.domain.model.HeartRate
import com.example.coursework.presentation.ViewM.HealthViewEvent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.heartRate.HearRateCardByDay
import com.example.coursework.presentation.components.heartRate.HeartRateCard
import com.example.coursework.presentation.components.heartRate.dayForLazyRowHeartRate
import com.example.coursework.presentation.components.heartRate.lazyRowProgressForHeartRate
import com.example.coursework.util.heartRateMeasure.LineChart
import com.example.coursework.util.heartRateMeasure.ScannerScreen
import java.time.format.DateTimeFormatter

@Composable
fun MeasureHeartRateScreen(viewModel: HealthViewModel) {

    ScannerScreen(
        viewModel._scanEnabled,
        onMeasured = {
            viewModel.handleViewEvent(
                HealthViewEvent.Update(
                    viewModel.currentDay.value.copy(
                        heartRateList = viewModel.currentDay.value.heartRateList + HeartRate(
                            it
                        )
                    )
                )
            )
            viewModel._scanEnabled.value = false
        },
        viewModel
    )

    val color = Color(168, 255, 65, 215)
    var current by remember { mutableStateOf(getCurrentDay()) }
    var currentDay by remember { mutableStateOf(viewModel.days.find { it.date == getCurrentDay() }!!) }
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {

            current = lazyRowProgressForHeartRate(
                color = color,
                onClick = {},
                days = viewModel.days.map {
                    dayForLazyRowHeartRate(
                        date = it.date,
                        max = it.heartRateList.maxByOrNull { it.value }?.value?:1,
                        min = it.heartRateList.minByOrNull { it.value }?.value?:1,
                    )
                })
            currentDay = viewModel.days.find { it.date == current }!!
        }
        item {
            if (viewModel.showGraph.value)
                LineChart(viewModel)
        }
        item {
            HearRateCardByDay(
                min = currentDay.heartRateList.minByOrNull { it.value }?.value ?: 0,
                max = currentDay.heartRateList.maxByOrNull { it.value }?.value ?: 0,
                last = currentDay.heartRateList.lastOrNull()?.value ?: 0
            )
            Button(onClick = {
                viewModel._scanEnabled.value = true
                viewModel.showGraph.value = true
                viewModel.heartRateValues.value = mutableListOf()
            }) {
                Text(text = "Измерить")
            }
            Button(onClick = {
                viewModel._scanEnabled.value = true
                viewModel.heartRateValues.value = mutableListOf()
                viewModel.checkCamera.value = true
                viewModel.showGraph.value = false
            }) {
                Text(text = "Проверить камеру")
            }
        }
        items(currentDay.heartRateList.reversed()) { heartRateValue ->
            HeartRateCard(
                heartRateValue.value, heartRateValue.heartRateMeasureTime.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            )
        }
    }
}
