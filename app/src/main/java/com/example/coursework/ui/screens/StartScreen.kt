package com.example.coursework.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel


@Composable
fun StartScreen(modifier: Modifier, navController: NavController, viewModel: HealthViewModel) {
    Column(
        modifier
            .padding(8.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (viewModel.uiState.stepsVisible) {
            CardSteps(
                onCardClick = { navController.navigate("steps_screen") },
                viewModel
            )

        }
        if (viewModel.uiState.waterVisible) {
            CardWater(
                onCardClick = { navController.navigate("water_screen") },
                viewModel
            )
        }
        if (viewModel.uiState.eatVisible) {
            CardEat(
                onCardClick = { navController.navigate("eat_screen") },
                viewModel = viewModel
            )
        }
        if (viewModel.uiState.activityVisible) {
            CardActivity(
                viewModel,
                onCardClick = { navController.navigate("activity_list") },
            )
        }
        if (viewModel.uiState.bodyCompositionVisible) {
            CardBMI(
                onClick = { navController.navigate("settings") },
                viewModel = viewModel
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(number: Int, title: String = "", onDismiss: (Int) -> Unit) {
    var num by remember {
        mutableStateOf("$number")
    }
    AlertDialog(onDismissRequest = { }) {
        Card {
            Column(
                Modifier
                    .padding(20.dp), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )

                TextField(
                    value = num,
                    onValueChange = { num = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        modifier = Modifier
                            .clickable { onDismiss(0) },
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = stringResource(id = R.string.save),
                        modifier = Modifier
                            .clickable { onDismiss(num.toIntOrNull() ?: 0) },
                        style = MaterialTheme.typography.displaySmall
                    )
                }

            }
        }
    }
}
