package com.example.coursework.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SelectNotificationsPeriod(viewModel: HealthViewModel) {
    val sharedPreferences = viewModel.provideSharedPreference()
    val days = viewModel.getDaysToNotification()
    var startTime by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "startTime",
                "09:00"
            )!!
        )
    }
    var period by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "NotificationPeriod",
                "02:00"
            )!!
        )
    }
    val color1 = Color(0, 144, 255, 222)
    val color2 = Color(71, 71, 71, 150)
    var endTime by remember { mutableStateOf(sharedPreferences.getString("endTime", "21:00")!!) }
    var showNotifications by remember {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "showNotifications",
                false
            )
        )
    }
    var showAlertDialogToSelectPeriod by remember { mutableStateOf(false) }
    var sunday by remember { mutableStateOf(days[0]) }
    var monday by remember { mutableStateOf(days[1]) }
    var tuesday by remember { mutableStateOf(days[2]) }
    var wednesday by remember { mutableStateOf(days[3]) }
    var thursday by remember { mutableStateOf(days[4]) }
    var friday by remember { mutableStateOf(days[5]) }
    var saturday by remember { mutableStateOf(days[6]) }
    val timeDialogStateStart = rememberMaterialDialogState()
    val timeDialogStateEnd = rememberMaterialDialogState()
    val textSize = 37

    fun saveDays() = sharedPreferences.edit().apply {
        putBoolean("monday", monday)
        putBoolean("tuesday", tuesday)
        putBoolean("wednesday", wednesday)
        putBoolean("thursday", thursday)
        putBoolean("friday", friday)
        putBoolean("saturday", saturday)
        putBoolean("sunday", sunday)
        apply()
    }

    if (showAlertDialogToSelectPeriod) {
        alertDialogSelectNotificationsInterval(edit = period) {
            if (it != "" && it.toSet().size>2) {
                period = it
                sharedPreferences.edit().putString("NotificationPeriod", it).apply()
            }
            showAlertDialogToSelectPeriod = false
        }
    }

    PickTime(
        edit = startTime,
        text = "select start time",
        dateDialogState = timeDialogStateStart
    ) {
        if (it.toString() < endTime) {
            startTime = it.toString()
            sharedPreferences.edit().putString("startTime", startTime).apply()
        }
    }
    PickTime(
        edit = endTime,
        text = "select end time",
        dateDialogState = timeDialogStateEnd
    ) {
        if (startTime < it.toString()) {
            endTime = it.toString()
            sharedPreferences.edit().putString("endTime", endTime).apply()
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
            Text(text = "Send notification", style = MaterialTheme.typography.headlineMedium)
            Checkbox(checked = showNotifications, onCheckedChange = {
                if (viewModel.requestPermission()) {
                    showNotifications = it
                    sharedPreferences.edit().putBoolean("showNotifications", it).apply()
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
                    text = "$startTime ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable {
                        timeDialogStateStart.show()
                    })
                Text(
                    text = " $endTime",
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
                text = stringResource(R.string.hours, period),
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
                clicked = monday
            ) {
                monday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "T",
                clicked = tuesday
            )
            {
                tuesday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "W",
                clicked = wednesday
            ) {
                wednesday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "T",
                clicked = thursday
            ) {
                thursday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "F",
                clicked = friday
            ) {
                friday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "S",
                clicked = saturday
            ) {
                saturday = it
                saveDays()
            }
            CircledText(
                size = textSize,
                color2 = color2,
                color = color1,
                text = "S",
                clicked = sunday
            ) {
                sunday = it
                saveDays()
            }
        }

    }
}


@Composable
fun PickTime(
    edit: String,
    text: String,
    dateDialogState: MaterialDialogState,
    onDismiss: (LocalTime) -> Unit
) {

    var time by remember { mutableStateOf(parceTime(edit)) }
    LaunchedEffect(edit) {
        time = parceTime(edit)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok), onClick = { onDismiss(time) })

        }
    ) {
        timepicker(
            initialTime = time,
            is24HourClock = true,
            title = text
        ) {
            time = it
        }
    }
}


@Composable
fun CircledText(
    size: Int,
    color: Color,
    color2: Color = Color.Gray,
    text: String,
    clicked: Boolean,
    onClick: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick(!clicked) }
            .size(size.dp)
            .background(
                color = if (clicked) {
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

fun parceTime(time: String) = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))


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
                            onDismiss(
                                "0$h:${
                                    if (m < 10) {
                                        "0$m"
                                    } else {
                                        "$m"
                                    }
                                }"
                            )
                        },
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

    }
}

@Composable
fun ScrolableLazyColumn(
    num: Int,
    listSize: Int,
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
        items((initialIndex..listSize).toList()) { index ->
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

