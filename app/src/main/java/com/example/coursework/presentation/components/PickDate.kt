package com.example.coursework.presentation.components

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.coursework.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


//диалог для выбора даты рождения можно выбрать дату только меньше текущей
@Composable
fun PickDate(edit: String, dateDialogState: MaterialDialogState, onDismiss: (Date) -> Unit) {

    var date by remember { mutableStateOf(Date()) }
    LaunchedEffect(edit) {
        date = SimpleDateFormat("dd/MM/yyyy").parse(edit)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok), onClick = { onDismiss(date) })

        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = stringResource(R.string.enter_birthday_date)
        ) {
            date = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }
    }
}