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
import com.example.coursework.presentation.components.dayForLazyRow
import com.example.coursework.presentation.components.heartRate.HearRateCardByDay
import com.example.coursework.presentation.components.heartRate.HeartRateCard
import com.example.coursework.presentation.components.lazyRowProgress1
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
                    viewModel.currentDay.copy(
                        heartRateList = viewModel.currentDay.heartRateList + HeartRate(
                            it
                        )
                    )
                )
            )
//            viewModel.currentDay.heartRateList += HeartRate(it)
            viewModel._scanEnabled.value = false
        },
        viewModel
    )

    val color = Color(168, 255, 65, 215)
    var current by remember { mutableStateOf(getCurrentDay()) }
    var currentDay by remember { mutableStateOf(viewModel.days.find { it.date == getCurrentDay() }!!) }
    val target = 100
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            current = lazyRowProgress1(
                target = target,
                color = color,
                onClick = {},
                days = viewModel.days.map {
                    dayForLazyRow(
                        it.date,
                        it.heartRateList.sumOf { it.value } / if (it.heartRateList.size != 0) it.heartRateList.size else 1,
                        target
                    )
                })
            currentDay = viewModel.days.find { it.date == current }!!
        }
        item {
            LineChart(viewModel)
        }
        item {
            HearRateCardByDay(min =  viewModel.currentDay.heartRateList.minByOrNull { it.value }?.value?:0,
            max = viewModel.currentDay.heartRateList.maxByOrNull { it.value }?.value?:0,)
            Button(onClick = {
                viewModel._scanEnabled.value = true
                viewModel.heartRateValues.value = mutableListOf()
            }) {
                Text(text = "Измерить")
            }
//            Text(text = "${viewModel.currentDay.heartRateList.lastOrNull()?.value ?: "Нет значения"}")
//            if (viewModel.currentDay.heartRateList.isNotEmpty()) {
//                Text(text = "${viewModel.currentDay.heartRateList.lastOrNull()?.heartRateMeasureTime.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Нет данных"}")
//            }
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
