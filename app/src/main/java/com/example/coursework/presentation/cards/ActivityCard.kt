package com.example.coursework.presentation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewEvent
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.AlertDialogForActivity
import com.example.coursework.presentation.components.IconForActivities


//карточка активности
@Composable
fun CardActivity(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var buttonIsClicked by remember { mutableStateOf("") }//нажата ли кнопка для ввода
    val activities = listOf("Skiing", "Hiking", "Sprint", "Martial") //список активностей
    if (buttonIsClicked in activities) {
        AlertDialogForActivity(type = buttonIsClicked) {
            if (it.duration > 0) {
                viewModel.handleViewEvent(
                    HealthViewEvent.Update(
                        viewModel.currentDay.copy(
                            activity = viewModel.currentDay.activity + it.duration
                        )
                    )
                )
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(trainings = viewModel.currentDay.trainings+it)))
            }
            buttonIsClicked = ""
        }
    }
    Card {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconForActivities(R.drawable.downhill_skiing_24px, onClick = {
                buttonIsClicked = "Skiing"
            })
            IconForActivities(R.drawable.hiking_24px, onClick = { buttonIsClicked = "Hiking" })
            IconForActivities(R.drawable.sprint_24px, onClick = { buttonIsClicked = "Sprint" })

            IconForActivities(drawable = R.drawable.list_24px) {
                onCardClick()
            }
        }
    }
}
