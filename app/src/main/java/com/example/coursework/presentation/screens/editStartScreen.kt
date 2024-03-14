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
            var visible by remember { mutableStateOf(viewModel.uiState.stepsVisible) }
            CardSteps(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible = it
                viewModel.uiState.stepsVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.uiState.waterVisible) }
            CardWater(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible = it
                viewModel.uiState.waterVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.uiState.eatVisible) }
            CardEat(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.uiState.eatVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.uiState.activityVisible) }
            CardActivity(onCardClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.uiState.activityVisible = it
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            var visible by remember { mutableStateOf(viewModel.uiState.bodyCompositionVisible) }
            CardBMI(onClick = { }, viewModel = viewModel)
            editStartScreenButton(visible = visible) {
                visible= it
                viewModel.uiState.bodyCompositionVisible = it
            }
        }
    }
    viewModel.saveUiState()
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
