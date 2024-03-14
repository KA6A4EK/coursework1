package com.example.coursework.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun ScrolableLazyColumn(
    num: Int,  //стартовое значение
    listSize: Int,//размер списка
    pitch: Int,//шаг значений для часов 1 2 3 для минут 5 10 15
    initialIndex: Int = 0, // если надо выбирать значение ноль что надо поставить -1
    onDismiss: (Int) -> Unit
) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = maxOf(num-1-initialIndex, 0))
    val scope = rememberCoroutineScope()
    val itemHeight = 50.dp
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset


    LazyColumn(
        state = scrollState, modifier = Modifier
            .height(190.dp)
            .padding(20.dp)
    ) {
        items((initialIndex..listSize).toList(),
            key = {it} ) { index ->
            ScrollCard(n = index * pitch, h = itemHeight, isMiddle = index == firstIndex+1+initialIndex)
        }
    }
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
        }
        onDismiss((firstIndex+1+initialIndex) * pitch)
    }
}
