package com.example.coursework.presentation.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.CardWithLazyColumnForSelect
import com.example.coursework.presentation.components.PickDate
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.util.Date


//экран настроек или другой не придумал
@Composable
fun SettingsScreen(
    viewModel: HealthViewModel
) {
    var buttonIsClicked by remember { mutableStateOf("") }
    val dateDialogState = rememberMaterialDialogState()
    //в зависимости от значения вызвает определенный alertDialog если значение не пустое
    if (buttonIsClicked != "") {
        when (buttonIsClicked) {
            "weight" -> SnapToBlockList(num = viewModel.userWeight, listSize = 500, text = stringResource(
                R.string.kg
            )
            ) {
                buttonIsClicked = ""
                viewModel.userWeight = it
                viewModel.saveHealthData()
            }

            "height" -> SnapToBlockList(num = viewModel.userHeight, listSize = 300, text = stringResource(
                id = R.string.cm
            )) {
                buttonIsClicked = ""
                viewModel.userHeight = it
                viewModel.saveHealthData()

            }

            "gender" -> GenderAlertDialog(Gender = viewModel.gender) {
                buttonIsClicked = ""
                viewModel.gender = it
                viewModel.saveHealthData()
            }

            "birthdayDate" -> {
                dateDialogState.show()
                PickDate(dateDialogState = dateDialogState, edit = viewModel.birthdayDate) {
                    viewModel.birthdayDate = if (it < Date()) {
                        SimpleDateFormat("dd/MM/yyyy").format(it).toString()
                    } else {
                        viewModel.birthdayDate
                    }
                    viewModel.saveHealthData()
                    buttonIsClicked = ""
                }

            }

            "name" -> {
                FillNameAlertDialog(name = viewModel.name) {
                    if (it != "") {
                        viewModel.name = it
                        viewModel.saveHealthData()
                    }
                    buttonIsClicked = ""

                }
            }
            else -> {}
        }
    }
    Column {
        Text(
            text = stringResource(R.string.hello, viewModel.name),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    buttonIsClicked = "name"
                }
        )
        Card {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { buttonIsClicked = "gender" }) {
                Icon(
                    painterResource(id = R.drawable.person_24px),
                    contentDescription = "",
                    Modifier.size(38.dp)
                )
                Text(
                    text = if (viewModel.gender=="Male"){
                        stringResource(id = R.string.male)}else{
                        stringResource(id = R.string.female)},
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { buttonIsClicked = "height" }) {
                Icon(
                    painterResource(id = R.drawable.height_24px),
                    contentDescription = "",
                    Modifier.size(38.dp)
                )
                Text(
                    text = "${viewModel.userHeight}",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { buttonIsClicked = "weight" }) {
                Icon(
                    painterResource(id = R.drawable.weight_24px),
                    contentDescription = "",
                    Modifier.size(38.dp)
                )
                Text(
                    text = "${viewModel.userWeight}",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { buttonIsClicked = "birthdayDate" }) {
                Icon(
                    painterResource(id = R.drawable.cake_24px),
                    contentDescription = "",
                    Modifier.size(38.dp)
                )
                Text(
                    text = viewModel.birthdayDate,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(text = "Шагов за все время ${viewModel.days.sumOf { it.steps }}")
            Text(text = "Воды за все время ${viewModel.days.sumOf { it.water }}")
            Text(text = "Активности всего за все время ${viewModel.days.sumOf { it.activity }}")
            Text(text = "Шагов в среднем ${viewModel.days.sumOf { it.steps/viewModel.days.size }}")
        }

    }
}

//диалог для выбора пола
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderAlertDialog(Gender: String, onDismiss: (String) -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss(Gender) }) {
        Card {
            Column(
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(20.dp), verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(R.string.select_gender),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                Text(text = stringResource(R.string.male),
                    Modifier
                        .clickable { onDismiss("Male") }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White)
                Text(text = stringResource(R.string.female),
                    Modifier
                        .clickable { onDismiss("Female") }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White)
            }
        }
    }
}


//диалог для ввода имени
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillNameAlertDialog(name: String, onDismiss: (String) -> Unit) {
    var name by remember {
        mutableStateOf(name)
    }
    AlertDialog(onDismissRequest = { }) {
        Card {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp), verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(R.string.enter_name),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                OutlinedTextField(value = name, onValueChange = { name = it })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Text(
                        text = stringResource(id = R.string.cancel),
                        modifier = Modifier
                            .clickable { onDismiss("") },
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = stringResource(id = R.string.save),
                        modifier = Modifier
                            .clickable { onDismiss(name) },
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}

//диалог для выбора веса роста
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapToBlockList(num: Int, listSize: Int, text: String, onDismiss: (Int) -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss(num) }) {
        CardWithLazyColumnForSelect(num = num+1, listSize = listSize, text = text, pitch = 1) {
            onDismiss(it)
        }
    }
}

