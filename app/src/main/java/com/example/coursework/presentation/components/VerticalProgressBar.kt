package com.example.coursework.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp


@Composable
fun VerticalProgressBar(
    columnSize: Float = 1f,
    padding: Int = 10,
    width: Int = 12,
    percent: Float,
    percentMin: Float= 1f,
    h: Int,
    modifier: Modifier = Modifier,
    color: Color,
    show : Boolean = true,
) {
    Box {
        Canvas(
            modifier = modifier
                .padding(padding.dp)
                .height(h.dp)

        ) {
            if (percent != 0f&&show) {
                drawLine(
                    color = color,
                    start =  Offset(0f, size.height *  percentMin ),
                    end = Offset(0f, size.height * (1 - percent / columnSize)),
                    strokeWidth = width.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

