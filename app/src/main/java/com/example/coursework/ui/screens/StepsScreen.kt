package com.example.coursework.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.model.Day
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Composable
fun StepsScreen(viewModel: HealthViewModel) {
    val target = viewModel.stepsTarget
    val days = viewModel.days.map { Pair(it.date,it.steps) }
    var steps by remember { mutableStateOf(0) }
    Column() {
        steps= lazyRowProgress(target = target, color = Color.Green, onClick ={}, days = days)
        Card(Modifier.padding(10.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$steps",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Text(text = " steps", style = MaterialTheme.typography.headlineMedium)
                }
                ProgressBar(
                    percent = steps / target.toFloat(), barWidth = 30, width = 290
                )
//                LinearProgressIndicator(steps/stepsTarget.toFloat(), trackColor = Color.Black, strokeCap = StrokeCap.Round,modifier =Modifier.height(30.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Text(
                        text = "${round(steps * 0.7 / 1000 * 100) / 100} km",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${round(steps * 0.04 * 100) / 100} Cal",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

    }
}


fun generateLast100Days(): List<Pair<String, Int>> {
    val today = LocalDate.now()
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM")
    return List(100) { index ->
        val date = today.minusDays(index.toLong())
        val num = (100..10000).random()
        Pair(date.format(dateFormat), num)
    }
}

@Composable
fun VerticalProgressBar(percent: Float, h: Int, color: Color) {
    Box {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .height(h.dp)

        ) {
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(0f, size.height * (1 - percent)),
                strokeWidth = 15.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
fun lazyRowProgress(target: Int, color: Color, onClick: (String) -> Unit,days :List<Pair<String,Int>>): Int {

    var current by remember {
        mutableStateOf(getCurrentDay())
    }

    LazyRow(
        contentPadding = PaddingValues(8.dp),
        reverseLayout = true
    ) {
        items(days, key = { it }) { date ->
            Column(
                Modifier
                    .padding(7.dp)
                    .height(150.dp)
                    .width(35.dp)
                    .clickable {
                        current = date.first
                        onClick.invoke(current)
                    },
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalProgressBar(
                    percent = date.second / target.toFloat(),
                    h = 100,
                    color = color
                )
                Box(Modifier.size(45.dp), contentAlignment = Alignment.Center) {
                    if (date.first == current) {
                        Text(
                            text = date.first.substring(0,5),
                            color = Color.Black,
                            modifier = Modifier.background(Color.Green, CircleShape),
                            fontSize = 13.sp
                        )
                    } else {
                        Text(
                            text = date.first.substring(0, 2),
                            color = Color.White,
                            fontSize = 19.sp
                        )
                    }
                }
            }

        }
    }
    return days.find { it.first==current }?.second ?: 404

}
fun getCurrentDay() =LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))