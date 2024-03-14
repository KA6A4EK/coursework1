package com.example.coursework.presentation.cards

import androidx.compose.runtime.Composable
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.StartScreenCard

//карточка показывающая данные сна
@Composable
fun SleepCard(viewModel: HealthViewModel) {
    StartScreenCard(
        onCardClick = { /*TODO*/ },
        onButtonClick = { /*TODO*/ },
        cardValue = (viewModel.sleepTime / 3600).toInt(),
        target = 9,
        text1 = "sleep time",
        text2 = ""
    ) {

    }
}