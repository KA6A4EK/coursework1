package com.example.coursework.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.core.text.isDigitsOnly
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.round

@Composable
fun StepsScreen(viewModel: HealthViewModel) {
    val target = viewModel.stepsTarget
    val color = Color(168, 255, 65, 215)
    val days = viewModel.days.map { Pair(it.date, it.steps) }
    var steps by remember { mutableIntStateOf(0) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        steps = lazyRowProgress(target = target, color = color, onClick = {}, days = days)
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
                    percent = steps / target.toFloat(), barWidth = 30, width = 290,color
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Text(
                        text = "${round(steps * 0.7 / 1000 * 100) / 100}${stringResource(R.string.km)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${round(steps * 0.04 * 100) / 100}${stringResource(R.string.cal)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

    }
}


@Composable
fun VerticalProgressBar(columnSize:Float=1f,percent: Float, h: Int, color: Color) {
    Box {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .height(h.dp)

        ) {
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(0f, size.height * (1 - percent/columnSize)),
                strokeWidth = 12.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

fun getCurrentDay() = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

@Composable
fun BoxWithProgress(target: Int,columnSize: Float) {
    Box(modifier = Modifier.height(210.dp), contentAlignment = Alignment.BottomStart) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {

            drawLine(
                color = Color.Green,
                start = Offset(0f, size.height *(1-1/columnSize) ),
                end = Offset(size.width, size.height *(1-1/columnSize)),
                strokeWidth = 1.dp.toPx(),
            )
//            val s =size.height *(1-1/columnSize)+( size.height -  size.height *(1-1/columnSize))/2-12
            val s = size.height*(1-1/(2*columnSize))
            drawLine(
                color = Color.Yellow,
                start = Offset(0f, s),
                end = Offset(size.width, s),
                strokeWidth = 1.dp.toPx(),
            )
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 13.sp.toPx()
                color = android.graphics.Color.WHITE
            }

            drawContext.canvas.nativeCanvas.drawText(
                "$target",
                size.width - 100, size.height *(1-1/columnSize)+33f,
                paint
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${(target / 2)}",
                size.width - 100, s + 33f,
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
    var current by remember { mutableStateOf(getCurrentDay()) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
//    var daysMaxValue by remember { mutableStateOf(scrollState.layoutInfo.totalItemsCount) }
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
    val daysMaxValue =  scrollState.layoutInfo.visibleItemsInfo.map { it.key }.toString().replace(")", "").replace("]", "").split(", ").filter { it.isDigitsOnly() }.map { it.toInt() }.maxOrNull()?:1
    val columnSize = max(daysMaxValue/target.toFloat(),1f)

    Box {
        BoxWithProgress(target,columnSize)
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
                                Color(red = 124, green = 124, blue = 124, alpha = 100)
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
                        columnSize,
                        percent = date.second / target.toFloat(),
                        h = 150,
                        color = color
                    )
                    Box(Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                        if (date.first == current) {
                            Text(
                                text = date.first.substring(0, 5),
                                color = Color.Black,
                                modifier = Modifier.background(color,CircleShape),
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

    Log.e(TAG,daysMaxValue.toString())
    Log.e(TAG,columnSize.toString())


    return days.find { it.first == current }?.second ?: 404
}
