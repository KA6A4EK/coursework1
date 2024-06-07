package com.example.coursework.presentation.components.heartRate

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.R

@Composable
fun HearRateCardByDay(max: Int, min: Int, last: Int) {
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .fillMaxWidth()
                .heightIn(110.dp),
        ) {
            Text(
                text = if (max != min) {
                    "$min-$max ${stringResource(R.string.bpm)}"
                } else {
                    "Нет значений"
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
            )

            heartRateProgressBar(
                barWidth = 25,
                width = 290,
                min = min,
                max = max,
                last = last,
                lineColor = Color(255, 80, 0, 255)
            )

        }
    }
}

@Composable
fun heartRateProgressBar(
    barWidth: Int, //ширина прогресс бара
    width: Int, //толщина линии
    lineColor: Color, // цвет линии
    min: Int,
    max: Int,
    last: Int,
) {

    val minPercent = roundToInterval((min - 40) / 180.toFloat())
    val maxPercent = roundToInterval((max - 40) / 180.toFloat())
    val lastPercent = roundToInterval((last - 40) / 180.toFloat())
    Box(modifier = Modifier.padding(bottom = 20.dp)) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .width(width.dp)
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = barWidth.dp.toPx(),
                cap = StrokeCap.Round
            )
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 17.sp.toPx()
                color = android.graphics.Color.WHITE
            }
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 18f), 0f)


            drawLine(
                color = lineColor,
                start = Offset(size.width * minPercent, 0f),
                end = Offset(size.width * maxPercent, 0f),
                strokeWidth = (barWidth + 5).dp.toPx(),
                cap = StrokeCap.Round,
            )
            if (last != 0) {
                drawLine(
                    color = Color(0, 0, 0, 255),
                    start = Offset(size.width * lastPercent, 30f),
                    end = Offset(size.width * lastPercent, -30f),
                    strokeWidth = (2).dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = pathEffect
                )
                drawContext.canvas.nativeCanvas.drawText(
                    "$last",
                    size.width * lastPercent - 25, 90f,
                    paint,
                    )
            }
        }
    }
}
//40-220

@Preview(showSystemUi = true)
@Composable
fun HearRateCardByDayPrew() {
    HearRateCardByDay(min = 3, max = 0, last = 80)
}

fun roundToInterval(value: Float): Float {
    return when {
        value < 0f -> 0f
        value > 1f -> 1f
        else -> value
    }
}