package com.example.coursework.presentation.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.PickTime
import com.example.coursework.presentation.components.ScrolableLazyColumn
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun SelectNotificationsPeriod(viewModel: HealthViewModel) {
    val color1 = Color(0, 144, 255, 222)
    val color2 = Color(71, 71, 71, 150)
    var showAlertDialogToSelectPeriod by remember { mutableStateOf(false) }//можно ли показать диалог
    val timeDialogStateStart = rememberMaterialDialogState()
    val timeDialogStateEnd = rememberMaterialDialogState()
    val user = remember { mutableStateOf(viewModel.user.value) }

    val timeStart = remember{ mutableStateOf(user.value.userSettings.notificationsSettings.notificationsPeriodStart)}
    val timeEnd = remember{ mutableStateOf(user.value.userSettings.notificationsSettings.notificationsPeriodEnd)}

    val textSize = 37
    if (showAlertDialogToSelectPeriod) {
        //вызов диалога выбора времени
        alertDialogSelectNotificationsInterval(edit = user.value.userSettings.notificationsSettings.notificationsPeriod) {
            if (it != "" && it.toSet().size > 2) {
                viewModel.user.value.userSettings.notificationsSettings.notificationsPeriod = it
            }
            showAlertDialogToSelectPeriod = false
        }
    }

    PickTime(
        edit = timeStart.value,
        text = "select start time",
        dateDialogState = timeDialogStateStart
    ) {
        if (it.toString() < viewModel.user.value.userSettings.notificationsSettings.notificationsPeriodEnd) {
            viewModel.user.value.userSettings.notificationsSettings.notificationsPeriodStart =
                it.toString()
            timeStart.value = it.toString()
        }
    }
    PickTime(
        edit = timeEnd.value,
        text = "select end time",
        dateDialogState = timeDialogStateEnd
    ) {
        if (viewModel.user.value.userSettings.notificationsSettings.notificationsPeriodStart < it.toString()) {
            viewModel.user.value.userSettings.notificationsSettings.notificationsPeriodEnd =
                it.toString()
            timeEnd.value = it.toString()

        }
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val clickedState = remember { mutableStateOf(user.value.userSettings.notificationsSettings.canSendNotifications) }

            Text(text = "Send notification", style = MaterialTheme.typography.headlineMedium)
            Checkbox(

            checked = clickedState.value,
                onCheckedChange = {
                    if (viewModel.requestPermission()) {
                        clickedState.value= it
                        viewModel.user.value.userSettings.notificationsSettings.canSendNotifications =
                            it
                    }
                })
        }
        Divider()
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Time", style = MaterialTheme.typography.headlineMedium)
            Row {
                Text(
                    text = "${user.value.userSettings.notificationsSettings.notificationsPeriodStart} ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable {
                        timeDialogStateStart.show()
                    })
                Text(
                    text = " ${user.value.userSettings.notificationsSettings.notificationsPeriodEnd}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable {
                        timeDialogStateEnd.show()
                    })
            }
        }
        Divider()
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.remind_me_every),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(
                    R.string.hours,
                    user.value.userSettings.notificationsSettings.notificationsPeriod
                ),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable {
                    showAlertDialogToSelectPeriod = true
                })
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceAround
        ) {
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "M",
                clicked = user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.monday
            ) {
                user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.monday =
                    it
                Log.e(TAG,it.toString())
                Log.e(TAG,viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.monday.toString())
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "T",
                clicked = user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.tuesday
            )
            {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.tuesday =
                    it
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "W",
                clicked = viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.wednesday
            ) {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.wednesday =
                    it
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "T",
                clicked = viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.thursday
            ) {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.thursday =
                    it
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "F",
                clicked = viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.friday
            ) {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.friday =
                    it

            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "S",
                clicked = viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.saturday
            ) {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.saturday =
                    it
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "S",
                clicked = viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.sunday
            ) {
                viewModel.user.value.userSettings.notificationsSettings.CanSendNotificationsByDay.sunday = it
            }
        }

    }
}


//текст в круге для выбора дня в который отправлять уведомления
@Composable
fun CircledText(
    size: Int,
    color: Color,
    color2: Color = Color.Gray,
    text: String,
    clicked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val clickedState = remember { mutableStateOf(clicked) }

    Box(
        modifier = Modifier
            .clickable {
                clickedState.value=!clickedState.value
                onClick(clickedState.value) }
            .size(size.dp)
            .background(
                color = if (clickedState.value) {
                    color
                } else {
                    color2
                }, androidx.compose.foundation.shape.CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}


//диалог для выбора времени
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun alertDialogSelectNotificationsInterval(edit: String, onDismiss: (String) -> Unit) {
    val time = edit.split(":")
    var h by remember { mutableIntStateOf(time[0].toInt()) }
    var m by remember { mutableIntStateOf(time[1].toInt()) }
    androidx.compose.material3.AlertDialog(onDismissRequest = { onDismiss("") }) {
        Card() {
            Row {
                ScrolableLazyColumn(num = h, 6, 1, initialIndex = -1) { h = it }
                ScrolableLazyColumn(num = m / 5, 12, 5, initialIndex = -1) { m = it }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier
                        .clickable { onDismiss("") },
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = stringResource(id = R.string.save),
                    modifier = Modifier
                        .clickable {
                            Log.e(TAG, ("0$h:${(m + 100).toString().substring(1)}"))
                            onDismiss("0$h:${(m + 100).toString().substring(1)}")
                        },
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

    }
}

