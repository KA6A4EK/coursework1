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
fun LineChart(dataPoint: Float, i: Int) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .width(300.dp)
        ) {
            val strokeWidth = 3.dp.toPx()
            val startX = i.toFloat() * 3
            val startY = size.height / 2 + dataPoint
            val endX = i.toFloat() * 3
            val endY = size.height / 2 + dataPoint
            drawLine(
                Color(255, 21, 123, 240),
                Offset(startX, startY),
                Offset(endX, endY),
                strokeWidth
            )

        }
    }
}

