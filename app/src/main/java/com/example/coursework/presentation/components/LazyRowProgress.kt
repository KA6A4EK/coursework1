package com.example.coursework.presentation.components

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
import androidx.core.text.isDigitsOnly
import com.example.coursework.presentation.screens.getCurrentDay
import kotlinx.coroutines.launch
import kotlin.math.max


@Composable
fun lazyRowProgress(
    target: Int,
    color: Color,
    onClick: (String) -> Unit,
    days: List<Pair<String, Int>>
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
            current = days[firstIndex + 1].first

        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
            current = days[firstIndex].first

        }
    }
    val daysMaxValue =
        scrollState.layoutInfo.visibleItemsInfo.map { it.key }.toString().replace(")", "")
            .replace("]", "").split(", ").filter { it.isDigitsOnly() }.map { it.toInt() }
            .maxOrNull() ?: 1
    val columnSize = max(daysMaxValue / target.toFloat(), 1f)
    Box {
        BoxWithProgress(target, columnSize)
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(end = 45.dp),
            reverseLayout = true
        ) {
            items(days, key = { it }) { date ->
                val color = if (date.second<target){
                    Color.Gray}

                else{color}

                Column(
                    Modifier
                        .padding(7.dp)
                        .height(230.dp)
                        .width(45.sp.value.dp)
                        .background(
                            if (date.first == current) {
                                Color(red = 124, green = 124, blue = 124, alpha = 100)
                            } else (MaterialTheme.colorScheme.background.copy(alpha = 0f)),
                            CircleShape
                        )
                        .clickable {
                            current = date.first
                            onClick.invoke(current)
                        },
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalProgressBar(
                        columnSize,
                        percent = date.second / target.toFloat(),
                        h = 150,
                        color = color
                    )
                    Box(Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                        if (date.first == current) {
                            Text(
                                text = date.first.substring(0, 5),
                                color = Color.Black,
                                modifier = Modifier.background(color, CircleShape),
                                fontSize = 14.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = date.first.substring(0, 2),
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



@Composable
fun lazyRowProgress1(
    target: Int,
    color: Color,
    onClick: (String) -> Unit,
    days: List<dayForLazyRow>
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
    val daysMaxValue = (days.filter { day-> visibleDays.contains(day.date)}.map { it.value }).maxOrNull()
    val columnSize = max((daysMaxValue?:target)/ target.toFloat(), 1f)

    Box {
        BoxWithProgress(target, columnSize)
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(end = 45.dp),
            reverseLayout = true
        ) {
            items(days, key = { it.date }) { date ->
                val color = if (date.value<date.target*0.76){
                    Color.Gray}
                else{color}

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
                        columnSize,
                        percent = date.value / date.target.toFloat(),
                        h = 150,
                        color = color
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

data class dayForLazyRow(
    val date : String,
    val value :  Int,
    val target: Int,
)