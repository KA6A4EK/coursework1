package com.example.coursework.util.heartRateMeasure

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coursework.presentation.ViewM.HealthViewModel

@Composable
fun LineChart(viewModel: HealthViewModel) {
    val heartRateValues by viewModel.heartRateValues.collectAsState() // Получаем значения в виде State
    val animatedProgress = remember { Animatable(0.001f) }
    var average = 0f
    LaunchedEffect(animatedProgress.value) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(
                iterations = 1,
                animation = tween(durationMillis = 3000, easing = LinearEasing)
            )
        )
    }

    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp)
            .horizontalScroll(rememberScrollState())

    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .width(540.dp)
        ) {
            val strokeWidth = 3.dp.toPx()
            for (i in 0 until heartRateValues.size - 1) {
                average+= heartRateValues[i]
                val startX = i.toFloat() * 6 //
                val startY = (size.height / 2 + (average/i -heartRateValues[i])*60).toFloat()
                val endX = (i + 1).toFloat() * 6
                val endY =  (size.height / 2 + (average/i -heartRateValues[i+1])*60).toFloat()
                drawLine(
                    Color(255, 21, 123, 240),
                    Offset(startX, startY),
                    Offset(endX, endY),
                    strokeWidth
                )
            }
        }
    }
}


