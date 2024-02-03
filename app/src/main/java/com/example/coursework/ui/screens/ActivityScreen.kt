package com.example.coursework.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
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
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.model.Training

@Composable
fun ActivityScreen(viewModel: HealthViewModel) {
    val days = viewModel.days.map { Pair(it.date, it.activity) }
    Log.e(TAG, days.toString())
    Log.e(TAG, viewModel.days.toString())

    var current by remember { mutableStateOf(getCurrentDay()) }
    LazyColumn(
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            lazyRowProgress(
                target = viewModel.activityTarget,
                color = Color(46, 197, 122, 255),
                onClick = { current = it },
                days = days
            )
        }
        item {
            TotalActivityTime(time = viewModel.trainingActivity.filter { it.date == current }
                .sumOf { it.duration }, target = viewModel.activityTarget)
        }
        items(viewModel.trainingActivity.filter { it.date == current }) {
            CardActivityList(training = it)
        }
    }
}

@Composable
fun CardActivityList(training: Training) {
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

@Composable
fun TotalActivityTime(time: Int, target: Int) {
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