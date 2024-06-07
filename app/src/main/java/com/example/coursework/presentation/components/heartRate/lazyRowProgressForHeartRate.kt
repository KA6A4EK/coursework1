package com.example.coursework.presentation.components.heartRate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.presentation.components.BoxWithProgressForHeartRate
import com.example.coursework.presentation.components.VerticalProgressBar
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.coroutines.launch

@Composable
fun lazyRowProgressForHeartRate(
    color: Color,
    onClick: (String) -> Unit,
    days: List<dayForLazyRowHeartRate>
): String {

    var current by remember { mutableStateOf(getCurrentDay()) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
            current = days[firstIndex + 1].date

        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
            current = days[firstIndex].date

        }
    }

    val visibleDays = scrollState.layoutInfo.visibleItemsInfo.map { it.key }
    val daysMaxValue = (days.filter { day-> visibleDays.contains(day.date)}.map { it.max }).maxOrNull()?:81
    val daysMinValue = (days.filter { day-> visibleDays.contains(day.date)}.filter { it.min!=1 }.map { it.min }).minOrNull()?:39

    Box {
        BoxWithProgressForHeartRate(max= daysMaxValue, min = daysMinValue)
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(end = 45.dp),
            reverseLayout = true
        ) {
            items(days, key = { it.date }) { date ->
                val color = color

                Column(
                    Modifier
                        .padding(7.dp)
                        .height(230.dp)
                        .width(45.sp.value.dp)
                        .background(
                            if (date.date == current) {
                                Color(red = 124, green = 124, blue = 124, alpha = 100)
                            } else (MaterialTheme.colorScheme.background.copy(alpha = 0f)),
                            CircleShape
                        )
                        .clickable {
                            current = date.date
                            onClick.invoke(current)
                        },
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalProgressBar(
                        1f,
                        percent = (date.max/daysMaxValue.toFloat())/80*67,
                        percentMin = (daysMinValue/date.min.toFloat())/80*61,
                        h = 150,
                        color = color,
                        show = date.min!=1
                    )
                    Box(Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                        if (date.date == current) {
                            Text(
                                text = date.date.substring(0, 5),
                                color = Color.Black,
                                modifier = Modifier.background(color, CircleShape),
                                fontSize = 14.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = date.date.substring(0, 2),
                                color = Color.White,
                                fontSize = 19.sp
                            )
                        }
                    }
                }

            }
        }
    }
    return current
}


data class dayForLazyRowHeartRate(
    val date : String,
    val max :  Int,
    val min :  Int,
)
