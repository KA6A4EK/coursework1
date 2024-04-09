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


//карточка шагов
@Composable
fun CardSteps(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var buttonIsClicked by remember { mutableStateOf(false) }  //нажата ли кнопка ввода шагов
    var steps by remember { mutableIntStateOf(viewModel.currentDay.steps) }//шаги текущего дня
//    LaunchedEffect (viewModel.currentDay.steps){
//        steps = viewModel.currentDay.steps
//    }
    if (buttonIsClicked)
        AlertDialog(
            number = steps,
            title = stringResource(R.string.enter_steps)
        ) {
            if (it > 0) {
                steps = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(stepsAtTheDay = listOf(it))))
            }
            buttonIsClicked = false
        }

    StartScreenCard(
        showButtonAction = viewModel.permissionForSteps,
        onCardClick = { onCardClick() },
        onButtonClick = { buttonIsClicked = true },
        cardValue = steps,
        target = viewModel.currentDay.targets.steps,
        text1 = stringResource(R.string.step),
        text2 = stringResource(id = R.string.enter)
    ) {
        ProgressBar(
            percent = steps / viewModel.currentDay.targets.steps.toFloat(),
            barWidth = 10,
            width = 120,
            Color.Green
        )
    }
}




