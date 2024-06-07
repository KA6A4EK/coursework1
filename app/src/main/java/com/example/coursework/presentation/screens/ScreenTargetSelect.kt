package com.example.coursework.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.CardWithLazyColumnForSelect


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
                num = viewModel.currentDay.value.targets.steps / pitch, listSize = 500, text = stringResource(
                    id = R.string.steps_target
                ), pitch = pitch
            ) {
                viewModel.currentDay.value.targets.steps = it
                navigateUp()
            }
        }
        "{activity_list}" -> {
            val pitch = 5
            CardWithLazyColumnForSelect(
                num = viewModel.currentDay.value.targets.activity / pitch,
                listSize = 50,
                text = stringResource(R.string.activity_target),
                pitch = pitch
            ) {
                viewModel.currentDay.value.targets.activity = it
                navigateUp()
            }
        }
        "{eat_screen}" -> {
            val pitch = 50
            CardWithLazyColumnForSelect(
                num = viewModel.currentDay.value.targets.eat / pitch, listSize = 500, text = stringResource(
                    id = R.string.eat_target
                ), pitch = pitch
            ) {
                viewModel.currentDay.value.targets.eat = it
                navigateUp()
            }
        }
        "{water_screen}" -> {
            val pitch = 100
            CardWithLazyColumnForSelect(
                num = viewModel.currentDay.value.targets.water / pitch, listSize = 100, text = stringResource(
                    id = R.string.water_target
                ), pitch = pitch
            ) {
                viewModel.currentDay.value.targets.water = it
                navigateUp()
            }
        }
        "{bmi_screen}" -> {
            CardWithLazyColumnForSelect(
                num = viewModel.currentDay.value.targets.weight, listSize = 500, text = stringResource(
                    R.string.weight_target
                ), pitch = 1
            ) {
                viewModel.currentDay.value.targets.weight = it
                navigateUp()
            }
        }
        "{cupSize}" -> {
            val pitch = 10
            CardWithLazyColumnForSelect(
                num = viewModel.user.value.userSettings.CupSize / pitch, listSize = 500, text = stringResource(
                    id = R.string.set_cup_size
                ), pitch = pitch
            ) {
                viewModel.user.value.userSettings.CupSize = it
                navigateUp()
            }
        }
    }
}

