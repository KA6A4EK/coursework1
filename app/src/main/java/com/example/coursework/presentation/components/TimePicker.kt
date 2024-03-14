package com.example.coursework.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.example.coursework.util.parceTime
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

//диалог для выбора даты
@Composable
fun PickTime(
    edit: String,//время для редактирования
    text: String,
    dateDialogState: MaterialDialogState,
    onDismiss: (LocalTime) -> Unit
) {

    var time by remember { mutableStateOf(parceTime(edit)) }
    LaunchedEffect(edit) {
        time = parceTime(edit)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok), onClick = { onDismiss(time) })

        }
    ) {
        timepicker(
            initialTime = time,
            is24HourClock = true,
            title = text
        ) {
            time = it
        }
    }
}