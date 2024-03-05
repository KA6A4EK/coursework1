package com.example.coursework.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.presentation.ViewM.HealthViewModel
import kotlin.random.Random


@Composable
fun WeightScreen(viewModel: HealthViewModel) {
    Graph()

}

@Composable
fun Graph() {
    val pairsList = remember { generateRandomPairsList() }
    val width = remember { (pairsList.size * 61).dp }

    Column(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(width)
    ) {
        GraphCanvas(pairsList)
        GraphLegend(pairsList)
    }
}

@Composable
private fun GraphCanvas(pairsList: List<Pair<Int, Int>>) {
    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                //.padding(.dp)
        ) {
            val target = 80f
            val pitch = 46.dp.toPx()
            val strokeWidth = 3.dp.toPx()
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                color = Color.Yellow.toArgb()
            }

            pairsList.windowed(2) { (current, next) ->
                val startX = current.first * pitch+23.dp.toPx()
                val startY = size.height / 2 * (current.second / target)
                val endX = next.first * pitch+23.dp.toPx()
                val endY = size.height / 2 * (next.second / target)
                drawCircle(Color.Yellow, radius = 20f, Offset(startX,startY))
                drawLine(Color.Yellow, Offset(startX, startY), Offset(endX, endY), strokeWidth)
                drawLine(Color.Yellow, Offset(startX, startY), Offset(startX, size.height*2), 1f)
//                drawContext.canvas.nativeCanvas.drawText("${current.second}", startX, size.height / 3 * 2, paint)
                drawContext.canvas.nativeCanvas.drawText("${current.first}", startX, size.height / 3 * 2 - 30, paint)
            }
        }
    }
}

@Composable
private fun GraphLegend(pairsList: List<Pair<Int, Int>>) {
    Row(
        modifier = Modifier.background(Color.Gray),
    ) {
        pairsList.forEach {
            Column(modifier = Modifier.width(46.sp.value.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = it.second.toString())
                Text(text = it.first.toString())
            }
        }
    }
}

private fun generateRandomPairsList(): List<Pair<Int, Int>> {
    val pairsList = mutableListOf<Pair<Int, Int>>()
    repeat(90) {
        val secondNumber = Random.nextInt(60, 80)
        pairsList.add(Pair(it, secondNumber))
    }
    return pairsList
}
