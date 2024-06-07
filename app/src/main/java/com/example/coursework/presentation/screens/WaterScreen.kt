package com.example.coursework.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.dayForLazyRow
import com.example.coursework.presentation.components.lazyRowProgress1

@Composable
fun WaterScreen(viewModel: HealthViewModel) {
    val color = Color(0, 166, 255, 255)
    var current by remember { mutableStateOf(getCurrentDay()) }
    var selectedDay by remember { mutableStateOf(viewModel.currentDay) }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        current = lazyRowProgress1(
            target = selectedDay.value.targets.water,
            color = color,
            onClick = {},
            days = viewModel.days.map { dayForLazyRow(it.date,it.water,it.targets.water) })
        selectedDay.value = viewModel.days.find { it.date == current }!!

        Card(
            Modifier
                .padding(10.dp)


        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                drawWater(
                    width = 120, height = 130,
                    percent = minOf(selectedDay.value.water.toFloat() / selectedDay.value.targets.water, 0.93f),
                    target = selectedDay.value.targets.water
                )
                Column {
                    Text(
                        text = "${selectedDay.value.water}",
                        style = MaterialTheme.typography.displayMedium,
                    )
                    Text(
                        text = "${stringResource(id = R.string.target)} ${selectedDay.value.targets.water}",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun drawWater(width: Int, target: Int = 0, height: Int, percent: Float) {

    Canvas(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .padding(10.dp)
    ) {
        val path = Path()
        val path2 = Path()
        val p = 0.1f
        val up = size.height * (1 - percent)


        path.moveTo(size.width / 2, 0f)//середина верх
        path.lineTo(size.width, 0f)//право верх
        path.lineTo(size.width * (1 - p), size.height)//право низ
        path.lineTo(size.width * p, size.height)//лево низ
        path.lineTo(0f, 0f)//право верх
        path.lineTo(size.width / 2, 0f)//середина верх

        drawPath(path, Color.DarkGray, style = Fill)
//    path2.moveTo(0f, size.height)//низ лево
//    path2.lineTo((size.width - size.width * topWidthRatio*percent) / 2, size.height*percent)//верх лево
//    path2.lineTo(size.width - (size.width - size.width * topWidthRatio)*percent / 2, size.height*percent)// верх право
//    path2.lineTo(size.width, size.height)//низ право
//    path2.lineTo(0f, size.height)// дно


        path2.moveTo(size.width / 2, up)//середина верх
        path2.lineTo(size.width * (1 - p * (1 - percent)), up)//право верх
        path2.lineTo(size.width * (1 - p), size.height)//право низ
        path2.lineTo(size.width * p, size.height)//лево низ
        path2.lineTo(size.width * p * (1 - percent), up)//лево верх
        path2.lineTo(size.width / 2, up)//середина верх
        drawPath(path2, Color(0, 175, 255, 196), style = Fill)


        if (target != 0) {
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 13.sp.toPx()
                color = android.graphics.Color.WHITE
            }
            drawLine(
                color = Color.White,
                start = Offset(size.width - 40, 27f),
                end = Offset(size.width - 5, 27f),
                strokeWidth = 2.dp.toPx(),
            )
            drawContext.canvas.nativeCanvas.drawText(
                "$target",
                size.width - 120, 40f,
                paint
            )
            drawLine(
                color = Color.White,
                start = Offset(size.width - 55, size.height / 2),
                end = Offset(size.width - 20, size.height / 2),
                strokeWidth = 2.dp.toPx(),
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${target / 2}",
                size.width - 135, size.height / 2 + 10,
                paint
            )
        }

    }

}