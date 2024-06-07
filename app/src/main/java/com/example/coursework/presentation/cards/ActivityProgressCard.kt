package com.example.coursework.presentation.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.ProgressBar
import com.example.coursework.presentation.components.StartScreenCard


@Composable
fun ActivityProgressCard(viewModel: HealthViewModel, onCardClick: () -> Unit) {


    StartScreenCard(
        showButtonAction = false,
        onCardClick = { onCardClick() },
        onButtonClick = { },
        cardValue = viewModel.currentDay.value.trainings.sumOf { it.duration },
        target = viewModel.currentDay.value.targets.activity,
        text1 = stringResource(R.string.mins),
        text2 = stringResource(id = R.string.enter)
    ) {
        ProgressBar(
            percent =viewModel.currentDay.value.trainings.sumOf { it.duration } / viewModel.currentDay.value.targets.activity.toFloat(),
            barWidth = 10,
            width = 120,
            Color(255, 87, 34, 255)
        )
    }
}

