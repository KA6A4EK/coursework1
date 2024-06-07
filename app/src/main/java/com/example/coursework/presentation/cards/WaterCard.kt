package com.example.coursework.presentation.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewEvent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.StartScreenCard
import com.example.coursework.presentation.screens.drawWater


//карточка воды
@Composable
fun CardWater(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var water by remember { mutableIntStateOf(viewModel.currentDay.value.water) } // вода для текущего дня
    val cupSize = viewModel.user.value.userSettings.CupSize  //размер кружки
    StartScreenCard(
        onCardClick = { onCardClick() },
        onButtonClick = {
            water += viewModel.user.value.userSettings.CupSize
            viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.value.copy(water = water)))
        },
        cardValue = water,
        target = viewModel.currentDay.value.targets.water,
        text1 = stringResource(id = R.string.ml),
        text2 = "+${viewModel.user.value.userSettings.CupSize}${stringResource(id = R.string.ml)}"
    ) {
        drawWater(
            width = 75,
            height = 80,
            percent = minOf(1f, water.toFloat() / viewModel.currentDay.value.targets.water)
        )
    }
}