package com.example.coursework.util.heartRateMeasure

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LineChart(dataPoints: List<DataPoint>) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .width(3000.dp)
        ) {
            val strokeWidth = 3.dp.toPx()
            dataPoints.windowed(2) { (current, next) ->
                val startX = current.x / 6
                val startY = size.height / 2 + current.y
                val endX = next.x / 6
                val endY = size.height / 2 + next.y
                drawLine(
                    Color(255, 21, 123, 240),
                    Offset(startX, startY),
                    Offset(endX, endY),
                    strokeWidth
                )
                drawLine(
                    Color(255, 21, 123, 240),
                    Offset(startX, startY),
                    Offset(startX, size.height),//TODO delete
                    strokeWidth
                )
            }
        }
    }
}

