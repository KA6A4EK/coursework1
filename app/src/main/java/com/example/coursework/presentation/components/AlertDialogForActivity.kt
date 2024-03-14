package com.example.coursework.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.R
import com.example.coursework.domain.model.Training
import com.example.coursework.presentation.screens.getCurrentDay

//всплывающее окно для ввода активности
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogForActivity(
    type: String, //тип активности - бег,..
    onDismiss: (Training) -> Unit,  //действие при нажатии на закрытие или сохранение
) {
    var duration by remember { mutableStateOf("") }   //продолжительность активности
    AlertDialog(onDismissRequest = { }) {
        Card {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = type,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = duration,
                    onValueChange = { duration = it },
                    label = {
                        Text(
                            text = stringResource(R.string.enter_duration),
                            fontSize = 15.sp
                        )
                    })
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.clickable {
                            onDismiss(
                                Training(
                                    title = type,
                                    duration = 0,
                                    date = getCurrentDay(),
                                )
                            )
                        })
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.clickable {
                            onDismiss(
                                Training(
                                    title = type,
                                    duration = duration.toIntOrNull() ?: 1,
                                    date = getCurrentDay(),
                                )
                            )
                        })

                }
            }
        }

    }
}