package com.example.coursework.presentation.components.heartRate

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.coursework.R

@Composable
fun HearRateCardByDay(max: Int, min:Int) {
    Card {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$min-$max ${R.string.bpm}")
            heartRateProgressBar(
                barWidth = 30,
                width = 290,
                min = min,
                max = max ,
                color = Color(255, 80, 0, 255)
            )

        }
    }
//TODO график показывающий максималькое и минимально значение и последнее со временем
}

@Composable
fun heartRateProgressBar(
    barWidth: Int, //ширина прогресс бара
    width: Int, //толщина линии
    color: Color, // цвет линии
    min: Int,
    max: Int,
){
    val minPercent = (min+60)/180
    val maxPercent = (max+60)/180
    Canvas(
        modifier = Modifier
            .padding(10.dp)
            .width(width.dp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(0f, 0f),
            end = Offset(size.width , 0f),
            strokeWidth = barWidth.dp.toPx(),
            cap = StrokeCap.Round
        )
        if (min!=max)
        drawLine(
            color = color,
            start = Offset(size.width * minPercent, 0f),
            end = Offset(size.width * maxPercent, 0f),
            strokeWidth = (barWidth + 5).dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}
//40-220