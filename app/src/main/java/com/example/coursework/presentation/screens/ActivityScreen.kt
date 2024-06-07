package com.example.coursework.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.domain.model.Training
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.dayForLazyRow
import com.example.coursework.presentation.components.lazyRowProgress1

//экран для показа графика активности и списка активности за каждый день
@Composable
fun ActivityScreen(viewModel: HealthViewModel) {
    val days = viewModel.days.map { dayForLazyRow(it.date,it.trainings.sumOf { it.duration },it.targets.activity) } //список пар дата-время активности
    var current by remember { mutableStateOf(getCurrentDay()) } //выбранная дата
    var currentDay by remember { mutableStateOf(viewModel.currentDay) } //выбранная дата
    LazyColumn(
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            current =  lazyRowProgress1(
                target = currentDay.value.targets.activity,
                color = Color(46, 197, 122, 255),
                onClick = { current = it },
                days = days
            )
            currentDay.value = viewModel.days.find { it .date==current }?: viewModel.currentDay.value
        }

        item {
            TotalActivityTime(time = currentDay.value.trainings
                .sumOf { it.duration }, target = currentDay.value.targets.activity)
        }
        items(currentDay.value.trainings) {
            CardActivityList(training = it)
        }
    }
}

//карточка для списка активности содержит описание конкретной активности название, продолжительность, дата
@Composable
fun CardActivityList(
    training: Training
) {
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = training.title,
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(text = training.date, color = Color.White)
            }
            Text(
                text = training.duration.toString(),
                color = Color.White,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}


//карточка показывающая время активности за выбранный день и цель активности за этот день
@Composable
fun TotalActivityTime(
    time: Int, //время активности в выбранный день
    target: Int //цель активности в выбранный день
) {
    Card {
        Text(
            text = stringResource(R.string.this_day),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 15.dp, start = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$time", style = MaterialTheme.typography.displaySmall)
            Text(
                text = "/$target",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Gray
            )
        }
    }
}