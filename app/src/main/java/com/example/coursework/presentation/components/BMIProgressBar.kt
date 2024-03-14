package com.example.coursework.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlin.math.round


//прогресс бар показывающий в како зоне находится индекс массы тела и граничные показатели веса для каждой границы
@Composable
fun BMIProgress(
    width: Int, //максимальная ширина прогресс бара
    bodyHeight: Int, // рост
    bodyWeight: Int, //вес
) {
    val minWeight =
        round(18.5 * bodyHeight * bodyHeight / 10000)  //минимальный вес для нормальной зоны
    val maxWeight =
        round((25 * bodyHeight * bodyHeight / 10000).toDouble()) //максимальный вес для нормы
    val bmi = bodyWeight / bodyHeight.toFloat() / bodyHeight.toFloat() * 10000 // индекс массы тела
    Column {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .width(width.dp)
        ) {
            val w = (size.width / 2 + size.width / 2 * (bmi - 22) / 9)
            val h = when {
                w < 0 -> 0
                w > size.width -> size.width
                else -> w
            }

            drawLine(
                color = Color(255, 186, 8, 255),
                start = Offset(0f, 0f),
                end = Offset(size.width / 3, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(115, 179, 52, 255),
                start = Offset(size.width / 3, 0f),
                end = Offset(size.width / 3 * 2, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Red,
                start = Offset(size.width / 3 * 2, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            val path = Path().apply {
                moveTo(h.toFloat() - 25f, -60f)
                lineTo(h.toFloat() + 25f, -60f)
                lineTo(h.toFloat() + 0f, -10f)
                close()
            }
            drawPath(path, color = Color.Blue)
        }
        Row(modifier = Modifier.width(width.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "$minWeight")
            Text(text = "$maxWeight")
        }
    }
}