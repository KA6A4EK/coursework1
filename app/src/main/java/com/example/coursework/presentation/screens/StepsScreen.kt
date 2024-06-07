package com.example.coursework.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.ProgressBar
import com.example.coursework.presentation.components.VerticalProgressBar
import com.example.coursework.presentation.components.dayForLazyRow
import com.example.coursework.presentation.components.lazyRowProgress1
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.round

@Composable
fun StepsScreen(viewModel: HealthViewModel) {
    val target = viewModel.currentDay.value.targets.steps
    val color = Color(168, 255, 65, 215)
    var current by remember { mutableStateOf(getCurrentDay()) }
    var currentDay by remember { mutableStateOf(viewModel.days.find { it.date == getCurrentDay() }!!) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        current = lazyRowProgress1(
            target = currentDay.targets.steps,
            color = color,
            onClick = {},
            days = viewModel.days.map { dayForLazyRow(it.date,it.steps,it.targets.steps) })
        currentDay = viewModel.days.find { it.date == current }!!
        Card(Modifier.padding(10.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${currentDay.steps}",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.steps),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {


                    ProgressBar(
                        percent = currentDay.steps / target.toFloat(),
                        barWidth = 30,
                        width = 290,
                        color
                    )
                    Text(text = "${stringResource(R.string.target)}  ${viewModel.currentDay.value.targets.steps}",modifier = Modifier.padding(5.dp),
                        Color.Gray)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Text(
                        text = "${round(currentDay.steps * 0.7 / 1000 * 100) / 100}${
                            stringResource(
                                R.string.km
                            )
                        }",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${round(currentDay.steps * 0.04 * 100) / 100}${stringResource(R.string.cal)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        CardWithStepsAtEveryHour(currentDay.stepsAtTheDay)
    }
}


fun getCurrentDay() = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))



@Composable
fun CardWithStepsAtEveryHour(stepsAtTheDay: List<Int>) {
    val max = maxOf(stepsAtTheDay.max(), 1)
    var time by remember { mutableStateOf(Calendar.getInstance().time.hours) }
    var current by remember { mutableStateOf("${stepsAtTheDay[time]}") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${time}:00-${time + 1}:00 $current")

        LazyRow {
            itemsIndexed(stepsAtTheDay) { index, it ->
                val color = if (time == index) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.background
                }
                VerticalProgressBar(
                    width = 7,
                    percent = it / max.toFloat(),
                    color = Color.Green,
                    h = 70,
                    padding = 7,
                    modifier = Modifier
                        .clickable {
                            current = "$it"
                            time = index
                        }
                        .background(color, CircleShape)
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "0")
            Text(text = "6")
            Text(text = "12")
            Text(text = "18")
            Text(text = "h")
        }
    }
}
