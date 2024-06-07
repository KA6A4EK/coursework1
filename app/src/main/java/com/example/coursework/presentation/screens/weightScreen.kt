package com.example.coursework.presentation.screens

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.domain.model.Day
import com.example.coursework.presentation.ViewM.HealthViewModel
import kotlin.random.Random


@Composable
fun WeightScreen(viewModel: HealthViewModel) {
    var lastDayWeight = viewModel.currentDay.value.weight
    val list = mutableListOf(viewModel.currentDay.value)
    viewModel.days.forEach {
        if (it.weight != lastDayWeight) {
            list.add(it)
            lastDayWeight = it.weight
        }
    }
    Log.e(TAG, list.toList().toString())
//        for ( i in 0..viewModel.days.size-1){
//        if ( viewModel.days[i].weight!=viewModel.days[i-1].weight) }
    Graph(list.toList())

}

@Composable
fun Graph(list: List<Day>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val width = remember { maxOf(screenWidth,(list.size * 61).dp) }
    Column(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .width(width),
        horizontalAlignment = Alignment.End
    ) {
        GraphCanvas(list)
        GraphLegend(list)
    }
}

@Composable
private fun GraphCanvas(list: List<Day>) {
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

            for (i in 0..list.size - 2) {
                val startX = i * pitch + 23.dp.toPx()
                val startY = size.height / 2 * (list[i].weight / target)
                val endX = (i + 1) * pitch + 23.dp.toPx()
                val endY = size.height / 2 * (list[i + 1].weight / target)
                drawCircle(Color.Yellow, radius = 20f, Offset(startX, startY))
                drawLine(Color.Yellow, Offset(startX, startY), Offset(endX, endY), strokeWidth)
                drawLine(Color.Yellow, Offset(startX, startY), Offset(startX, size.height * 2), 1f)
//                drawContext.canvas.nativeCanvas.drawText("${current.second}", startX, size.height / 3 * 2, paint)
//                drawContext.canvas.nativeCanvas.drawText(
//                    "${current.first}",
//                    startX,
//                    size.height / 3 * 2 - 30,
//                    paint
//                )
            }
        }
    }
}

@Composable
private fun GraphLegend(list: List<Day>) {
    Row(
        modifier = Modifier.background(Color.Gray),
    ) {
        list.forEach {
            Column(
                modifier = Modifier.width(46.sp.value.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = it.weight.toString())
//                Text(text = it.first.toString())
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
