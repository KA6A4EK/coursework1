package com.example.coursework.presentation.cards

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.domain.model.Activities
import com.example.coursework.presentation.ViewM.HealthViewEvent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.AlertDialogForActivity
import com.example.coursework.presentation.components.IconForActivities


//карточка активности
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardActivity(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var buttonIsClicked by remember { mutableStateOf("") }//нажата ли кнопка для ввода
    var deleteActivity = remember { mutableStateOf(false) }//нажата ли кнопка для ввода
    if (buttonIsClicked != "") {
        AlertDialogForActivity(type = buttonIsClicked) {
            if (it.duration > 0) {
                viewModel.handleViewEvent(
                    HealthViewEvent.Update(
                        viewModel.currentDay.value.copy(trainings = viewModel.currentDay.value.trainings + it)
                    )
                )
            }
            buttonIsClicked = ""
        }
    }

    Card {
        LazyRow(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .heightIn(110.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Column(modifier = Modifier.padding(end = 5.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onCardClick() }
                            .size(70.dp)
                            .background(Color(93, 151, 87, 170))
                            .padding(bottom = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add, contentDescription = "", modifier = Modifier
                                .size(40.dp), Color.White
                        )
                    }
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            items(viewModel.user.value.userSettings.customActivities) {
                if (deleteActivity.value) {
                    BackHandler {
                        deleteActivity.value = false
                    }

                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconForActivities(
                            modifier = Modifier.clickable {
                                buttonIsClicked = it.title
                            },
                            drawable = it.icon,
                            description = it.title,
                        )
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "edit activity",
                            modifier = Modifier.clickable {
                                viewModel.user.value.userSettings.customActivities =
                                    viewModel.user.value.userSettings.customActivities.filter { id -> id.title != it.title }
                            })
                    }
                } else {

                    IconForActivities(
                        modifier = Modifier.combinedClickable(onClick = {
                            buttonIsClicked = it.title
                        }, onLongClick = {
                            deleteActivity.value = true
                        }),
                        drawable = it.icon,
                        description = it.title
                    )
                }
            }

            items(viewModel.user.value.userSettings.activitiesOrderBy) { activityId ->
                val activity = Activities.entries.find { it.id == activityId } ?: Activities.Sprint
                val title = stringResource(activity.title)
                IconForActivities(
                    modifier = Modifier.clickable {
                        buttonIsClicked = title
                    },
                    activity.icon,
                    description = title
                )
            }
        }
    }
}
