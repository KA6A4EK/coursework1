package com.example.coursework.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Composable
fun StepsScreen(viewModel: HealthViewModel) {
    val target = viewModel.stepsTarget
    val days = viewModel.days.map { Pair(it.date, it.steps) }
    var steps by remember { mutableIntStateOf(0) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        steps = lazyRowProgress(target = target, color = Color.Green, onClick = {}, days = days)
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
                    Text(
                        text = stringResource(R.string.steps),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                ProgressBar(
                    percent = steps / target.toFloat(), barWidth = 30, width = 290
                )
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
                strokeWidth = 12.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

fun getCurrentDay() = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

@Composable
fun BoxWithProgress(target: Int) {
    Box(modifier = Modifier.height(220.dp), contentAlignment = Alignment.BottomStart) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(111.dp)
        ) {

            drawLine(
                color = Color.Green,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx(),
            )
            drawLine(
                color = Color.Yellow,
                start = Offset(0f, size.height / 2f - 15f),
                end = Offset(size.width, size.height / 2f - 15f),
                strokeWidth = 1.dp.toPx(),
            )
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 13.sp.toPx()
                color = android.graphics.Color.WHITE
            }

            drawContext.canvas.nativeCanvas.drawText(
                "$target",
                size.width - 100, 37f,
                paint
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${(target / 2)}",
                size.width - 100, size.height / 2 + 20f,
                paint
            )
        }
    }
}

@Composable
fun lazyRowProgress(
    target: Int,
    color: Color,
    onClick: (String) -> Unit,
    days: List<Pair<String, Int>>
): Int {
    var current by remember {
        mutableStateOf(getCurrentDay())
    }
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
            current = days[firstIndex + 1].first

        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
            current = days[firstIndex].first

        }
    }
    Box {
        BoxWithProgress(target)
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(end = 45.dp),
            reverseLayout = true
        ) {
            items(days, key = { it }) { date ->
                Column(
                    Modifier
                        .padding(7.dp)
                        .height(230.dp)
                        .width(45.sp.value.dp)
                        .background(
                            if (date.first == current) {
                                Color(255, 255, 1, alpha = 100)
                            } else (MaterialTheme.colorScheme.background.copy(alpha = 0f)),
                            CircleShape
                        )
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
                    Box(Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                        if (date.first == current) {
                            Text(
                                text = date.first.substring(0, 5),
                                color = Color.Black,
                                modifier = Modifier.background(Color.Green, CircleShape),
                                fontSize = 14.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
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
    }

    return days.find { it.first == current }?.second ?: 404
}

@Composable
fun setStepsTarget (viewModel: HealthViewModel,onDismiss : (Boolean)->Unit){
    var buttonSetTarget by remember { mutableStateOf(false) }

    Text(
        text = stringResource(R.string.set_target),
        modifier = Modifier.clickable { buttonSetTarget = true },
        style = MaterialTheme.typography.headlineLarge
    )
    if (buttonSetTarget) {
        val pitch = 100
        targetSelectList(
            num = maxOf(viewModel.stepsTarget / pitch, 1),
            listSize = 500,
            text = stringResource(R.string.steps_target),
            pitch = pitch
        ) {
            viewModel.stepsTarget = it * pitch
            viewModel.saveHealthData()
            buttonSetTarget = false
            onDismiss(false)
        }
    }
}