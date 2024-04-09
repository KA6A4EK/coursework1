package com.example.coursework.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.cards.CardActivity
import com.example.coursework.presentation.cards.CardBMI
import com.example.coursework.presentation.cards.CardEat
import com.example.coursework.presentation.cards.CardSteps
import com.example.coursework.presentation.cards.CardWater


//экран для редактирования показа карточек какие показывать а какие нет
//каждая карточка помещается в BOX и сверху нее кнопка
@Composable
fun editStartScreen(viewModel: HealthViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(10.dp)) {
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.user.value.userSettings.healthUiState.stepsVisible) }
            CardSteps(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible = it
                viewModel.user.value.userSettings.healthUiState.stepsVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.user.value.userSettings.healthUiState.waterVisible) }
            CardWater(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible = it
                viewModel.user.value.userSettings.healthUiState.waterVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.user.value.userSettings.healthUiState.eatVisible) }
            CardEat(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.user.value.userSettings.healthUiState.eatVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.user.value.userSettings.healthUiState.activityVisible) }
            CardActivity(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.user.value.userSettings.healthUiState.activityVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.user.value.userSettings.healthUiState.bodyCompositionVisible) }
            CardBMI(onClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.user.value.userSettings.healthUiState.bodyCompositionVisible = it
            }
        }
    }
}


@Composable
fun editStartScreenButton(visible: Boolean, onClick: (Boolean) -> Unit) {
    Icon(
        if (visible) {
            Icons.Default.Close
        } else {
            Icons.Default.Add
        },
        contentDescription = "",
        modifier = Modifier.size(33.dp).clickable {
            onClick(!visible)
        })
}
