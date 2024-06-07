package com.example.coursework.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coursework.R
import com.example.coursework.domain.model.Activities
import com.example.coursework.domain.model.CustomActivity
import com.example.coursework.presentation.components.IconForActivities
import kotlinx.coroutines.launch

@Composable
fun CreateCustomActivityScreen(
    onSave: (CustomActivity?) -> Unit,
    navController: NavController,
) {
    var activityTitle by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf(Activities.Exercise.icon) }

    Column {


        TextField(
            value = activityTitle,
            label = { Text(text = stringResource(R.string.enter_title)) },
            onValueChange = { activityTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
        )

        ChoseActivityIcon {
            icon = it
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier
                    .clickable {
                        onSave(null)
                        navController.navigateUp()
                    },
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable {
                        onSave(CustomActivity(icon, activityTitle))
                        navController.navigateUp()
                    },
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

@Composable
fun ChoseActivityIcon(
    onClick: (Int) -> Unit,
): Int {
    val icons = Activities.entries.toTypedArray()
    var current by remember { mutableStateOf(Activities.Exercise.icon) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
            current = icons[firstIndex + 1].icon
            onClick(current)
        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
            current = icons[firstIndex].icon
            onClick(current)
        }
    }
    Box {

        LazyRow(
            state = scrollState,
            modifier = Modifier,
            reverseLayout = true
        ) {
            items(icons, key = { it.id }) { activity ->
                if (activity.icon == current) {
                    IconForActivities(
                        modifier = Modifier.clickable {
                            current = activity.icon
                            onClick(activity.icon)
                        },
                        activity.icon,
                        description = "",
                        color = Color(255, 193, 7, 255)
                    )
                } else {
                    IconForActivities(
                        modifier = Modifier.clickable {
                            current = activity.icon
                            onClick(activity.icon)
                        },                        activity.icon,
                        description = ""
                    )
                }

            }
        }
    }
    return current
}