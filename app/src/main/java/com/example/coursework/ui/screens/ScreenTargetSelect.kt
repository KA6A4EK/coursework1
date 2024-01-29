package com.example.coursework.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import kotlinx.coroutines.launch

@Composable
fun TargetScreen(viewModel: HealthViewModel, curent: String) {
    Text(text = curent)
}


//var cupSize: Int = sharedPrefs.getInt("cupSize", 200)
//var activityTarget: Int = sharedPrefs.getInt("activityTarget", 90)
//var eatTarget: Int = sharedPrefs.getInt("eatTarget", 2500)
//var weightTarget: Int = 100
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun targetSelectList(num: Int, listSize: Int, text: String, pitch: Int, onDismiss: (Int) -> Unit) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = num - 1)
    val scope = rememberCoroutineScope()
    val itemHeight = 50.dp
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset

    androidx.compose.material3.AlertDialog(onDismissRequest = { onDismiss(num) }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                LazyColumn(
                    state = scrollState, modifier = Modifier
                        .height(190.dp)
                        .padding(20.dp)
                ) {
                    items(listSize) { index ->
                        if (index == firstIndex + 1) {
                            ScrollCard(n = index * pitch, h = itemHeight, isMiddle = true)

                        } else {
                            ScrollCard(
                                n = index * pitch,
                                h = itemHeight,

                                modifier = Modifier.clickable {
                                    scope.launch {
                                        if (index != 0) {
                                            scrollState.animateScrollToItem(index - 1)
                                        }
                                    }
                                })
                        }
                    }
                }
                Column(
                    modifier = Modifier.height(190.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = text, style = MaterialTheme.typography.headlineMedium)
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier
                        .clickable { onDismiss(num) },
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = stringResource(id = R.string.save),
                    modifier = Modifier
                        .clickable { onDismiss(firstIndex + 1) },
                    style = MaterialTheme.typography.displaySmall
                )
            }
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
    }
}


@Composable
fun ScrollCard(num: Int, listSize: Int, text: String, pitch: Int, onDismiss: (Int) -> Unit) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = num - 1)
    val scope = rememberCoroutineScope()
    val itemHeight = 50.dp
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            LazyColumn(
                state = scrollState, modifier = Modifier
                    .height(190.dp)
                    .padding(20.dp)
            ) {
                items(listSize) { index ->
                    if (index == firstIndex + 1) {
                        ScrollCard(n = index * pitch, h = itemHeight, isMiddle = true)

                    } else {
                        ScrollCard(
                            n = index * pitch,
                            h = itemHeight,

                            modifier = Modifier.clickable {
                                scope.launch {
                                    if (index != 0) {
                                        scrollState.animateScrollToItem(index - 1)
                                    }
                                }
                            })
                    }
                }
            }
            Column(
                modifier = Modifier.height(190.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = text, style = MaterialTheme.typography.headlineMedium)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier
                    .clickable { onDismiss(num) },
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable { onDismiss(firstIndex + 1) },
                style = MaterialTheme.typography.displaySmall
            )
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
    }
}