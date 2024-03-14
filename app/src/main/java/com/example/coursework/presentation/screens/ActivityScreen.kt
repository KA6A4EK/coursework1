package com.example.coursework.presentation.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.domain.model.Training
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.dayForLazyRow
import com.example.coursework.presentation.components.lazyRowProgress1

//экран для показа графика активности и списка активности за каждый день
@Composable
fun ActivityScreen(viewModel: HealthViewModel) {
    val days = viewModel.days.map { dayForLazyRow(it.date,it.activity,it.targets.activity) } //список пар дата-время активности
    var current by remember { mutableStateOf(getCurrentDay()) } //выбранная дата
    var currentDay by remember { mutableStateOf(viewModel.currentDay) } //выбранная дата
    LazyColumn(
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            current =  lazyRowProgress1(
                target = currentDay.targets.activity,
                color = Color(46, 197, 122, 255),
                onClick = { current = it },
                days = days
            )
            currentDay = viewModel.days.find { it .date==current }?: viewModel.currentDay
        }

        item {
            TotalActivityTime(time = currentDay.trainings
                .sumOf { it.duration }, target = currentDay.targets.activity)
        }
        Log.e(TAG,"${currentDay.trainings}")
        items(currentDay.trainings) {
            CardActivityList(training = it)
        }
    }
}

//карточка для списка активности содержит описание конкретной активности название, продолжительность, дата
@Composable
fun CardActivityList(
    training: Training
) {
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = training.title,
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(text = training.date, color = Color.White)
            }
            Text(
                text = training.duration.toString(),
                color = Color.White,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}


//карточка показывающая время активности за выбранный день и цель активности за этот день
@Composable
fun TotalActivityTime(
    time: Int, //время активности в выбранный день
    target: Int //цель активности в выбранный день
) {
    Card {
        Text(
            text = stringResource(R.string.this_day),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 15.dp, start = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$time", style = MaterialTheme.typography.displaySmall)
            Text(
                text = "/$target",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Gray
            )
        }
    }
}

































@Composable
fun OrderInfo(order: Order, padding: PaddingValues, navController: NavController) {
    val showNextPageDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = padding.calculateTopPadding(), end = 10.dp, start = 10.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = ScrollState(0), enabled = true)
                .weight(1f, false)
        ) {
            ListItem(
                headlineContent = { Text(text = "Отправитель:") },
                supportingContent = {
                    Column {
                        Text(
                            text = if (order.sender.corpClient == null)
                                order.sender.name
                            else
                                order.sender.corpClient!!.title
                        )
                        Text(text = order.sender.phone.toString())
                        Text(text = order.transportation.cityFrom)
                    }
                },
                trailingContent = {
                    IconButton(onClick = { /*TODO ПЕРЕКЛЮЧЕНИЕ НА ЗВОНИЛКУ*/ }) {
                        Icon(imageVector = Icons.Outlined.Phone, contentDescription = "")
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text(text = "Получатель:") },
                supportingContent = {
                    Column {
                        Text(
                            text = if (order.consignee.corpClient == null)
                                order.consignee.name
                            else
                                order.consignee.corpClient!!.title
                        )
                        Text(text = order.consignee.phone.toString())
                        Text(text = order.transportation.cityTo)
                    }
                },
                trailingContent = {
                    IconButton(onClick = { /*TODO ПЕРЕКЛЮЧЕНИЕ НА ЗВОНИЛКУ*/ }) {
                        Icon(imageVector = Icons.Outlined.Phone, contentDescription = "")
                    }
                }
            )
            HorizontalDivider()
            ListItem(headlineContent = { Text(text = "Адрес: ${order.pickUp!!.address.address}") },
                supportingContent = {
                    Column {
                        Text(
                            text = "Даты: ${
                                order.pickUp!!.pickupsFrom.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd.MM.yyyy HH:mm"
                                    )
                                )
                            }" +
                                    " - " +
                                    order.pickUp!!.pickupsTo.format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                        Text(
                            text = if (order.pickUp!!.pickupsBreakFrom.hour == 0)
                                "Обед: не указан"
                            else
                                "Обед: ${
                                    order.pickUp!!.pickupsBreakFrom.format(
                                        DateTimeFormatter.ofPattern(
                                            "HH:mm"
                                        )
                                    )
                                }" +
                                        " - " +
                                        order.pickUp!!.pickupsBreakTo.format(
                                            DateTimeFormatter.ofPattern(
                                                "HH:mm"
                                            )
                                        )
                        )
                    }
                })
            HorizontalDivider()
            ListItem(headlineContent = {
                Row {
                    Text(text = "Количество мест:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = order.quantityOfCargo.toString())
                }
            })
            HorizontalDivider()
            ListItem(headlineContent = {
                Row {
                    Text(text = "Общая масса:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = order.cargoList.sumOf { cargo -> cargo.weight }.toString())
                }
            })
            HorizontalDivider()
            ListItem(headlineContent = {
                Row {
                    Text(text = "Общий объем:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = String.format("%.3f", order.getVolume()))
                }
            })
            HorizontalDivider()
            ListItem(headlineContent = {
                Row {
                    Text(text = "Объяв. стоимость:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = order.value.toString())
                }
            })
            HorizontalDivider()
            ListItem(headlineContent = { Text(text = "Комментарий:") },
                supportingContent = { Text(text = if (order.comment != null) order.comment!! else "нет") })
            HorizontalDivider()
        }
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                LuchButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, end = 5.dp)
                        .fillMaxWidth(),
                    text = "Отмена"
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                LuchButton(
                    onClick = { showNextPageDialog.value = true },
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, start = 5.dp)
                        .fillMaxWidth(),
                    text = "Начать"
                )
            }
        }
        if (showNextPageDialog.value)
            NextPageChoice(order, navController, showNextPageDialog)
    }
}
