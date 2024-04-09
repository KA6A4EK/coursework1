package com.example.coursework.presentation.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewEvent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.ProgressBar
import com.example.coursework.presentation.components.StartScreenCard
import com.example.coursework.presentation.screens.AlertDialog


//карточка еды
@Composable
fun CardEat(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var cal by remember { mutableIntStateOf(viewModel.currentDay.eat) } //калории в текущий день
    var buttonIsClicked by remember { mutableStateOf(false) }     //нажата ли кнопка ввода калорий
    if (buttonIsClicked) {
        AlertDialog(number = cal, title = stringResource(R.string.enter_calories)) {
            if (it > 0) {
                cal = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(eat = it)))
            }
            buttonIsClicked = false
        }

    }
    StartScreenCard(
        onCardClick = { onCardClick() },
        onButtonClick = {
            buttonIsClicked = true
        },
        cardValue = cal,
        target = viewModel.currentDay.targets.eat,
        text1 = stringResource(id = R.string.cal),
        text2 = stringResource(id = R.string.enter),
    ) {
        ProgressBar(
            percent = cal / viewModel.currentDay.targets.eat.toFloat(),
            barWidth = 10,
            width = 120,
            Color.Yellow
        )
    }
}
