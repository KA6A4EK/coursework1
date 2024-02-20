package com.example.coursework.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel


//экран для выбора целей  в зависимости от прошлого экрана
@Composable
fun TargetScreen(viewModel: HealthViewModel, curent: String,// текущий экран
                 navigateUp: () -> Unit//
) {
    Text(text = curent)
    when (curent) {
        "{steps_screen}" -> {
            val pitch = 100
            CardWithLazyColumnForSelect(
                num = viewModel.stepsTarget / pitch, listSize = 500, text = stringResource(
                    id = R.string.steps_target
                ), pitch = pitch
            ) {
                viewModel.stepsTarget = it
                navigateUp()
            }
        }
        "{activity_list}" -> {
            val pitch = 5
            CardWithLazyColumnForSelect(
                num = viewModel.activityTarget / pitch,
                listSize = 50,
                text = stringResource(R.string.activity_target),
                pitch = pitch
            ) {
                viewModel.activityTarget = it
                navigateUp()
            }
        }
        "{eat_screen}" -> {
            val pitch = 50
            CardWithLazyColumnForSelect(
                num = viewModel.eatTarget / pitch, listSize = 500, text = stringResource(
                    id = R.string.eat_target
                ), pitch = pitch
            ) {
                viewModel.eatTarget = it
                navigateUp()
            }
        }
        "{water_screen}" -> {
            val pitch = 100
            CardWithLazyColumnForSelect(
                num = viewModel.waterTarget / pitch, listSize = 100, text = stringResource(
                    id = R.string.water_target
                ), pitch = pitch
            ) {
                viewModel.waterTarget = it
                navigateUp()
            }
        }
        "{bmi_screen}" -> {
            CardWithLazyColumnForSelect(
                num = viewModel.weightTarget, listSize = 500, text = stringResource(
                    R.string.weight_target
                ), pitch = 1
            ) {
                viewModel.weightTarget = it
                navigateUp()
            }
        }
        "{cupSize}" -> {
            val pitch = 10
            CardWithLazyColumnForSelect(
                num = viewModel.cupSize / pitch, listSize = 500, text = stringResource(
                    id = R.string.set_cup_size
                ), pitch = pitch
            ) {
                viewModel.cupSize = it
                navigateUp()
            }
        }
    }
    viewModel.saveHealthData()
}

//карточка для выбора элемента
@Composable
fun CardWithLazyColumnForSelect(
    num: Int,  //начальный элемент
    listSize: Int,  //размер списка для выбора
    text: String,//текст оописание
    pitch: Int,///шаг между элеметами
    initialIndex: Int = 0,  //значение первого элемента если 0 то 0 и его выбрать нельзя если -1 то можно выбрать 0
    onDismiss: (Int) -> Unit
) {
    var ret by remember { mutableIntStateOf(num) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            ScrolableLazyColumn(
                num - 1 / pitch,
                listSize,
                pitch,
                initialIndex = initialIndex
            ) {
                ret = it
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
                    .clickable { onDismiss(num * pitch) },
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable { onDismiss(ret) },
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}