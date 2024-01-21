package com.example.coursework.ui.screens

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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.coursework.ViewM.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: HealthViewModel
) {
    var userWeight by remember { mutableIntStateOf(viewModel.userWeight) }
    var userHeight by remember { mutableIntStateOf(viewModel.userHeight) }
    var gender by remember { mutableStateOf(viewModel.gender) }
    var birthday by remember { mutableStateOf(viewModel.birthdayDate) }
    var buttonIsCliked by remember { mutableStateOf("") }
    if (buttonIsCliked != "") {
       when (buttonIsCliked) {
            "weight" -> alertDialog(number = userWeight) {
                buttonIsCliked = ""
                userWeight = it
                viewModel.userWeight = it
                viewModel.saveHealthData()

            }

            "height" -> alertDialog(number = userHeight) {
                buttonIsCliked = ""
                userHeight = it
                viewModel.userHeight = it
                viewModel.saveHealthData()

            }

            "gender" -> genderAlertDialog(Gender = gender) {
                buttonIsCliked = ""
                gender = it
                viewModel.gender = it
                viewModel.saveHealthData()
            }
            "birthdayDate" -> DatePickerDialog(
                onDismissRequest = { },
                confirmButton = { }) {

            }
            else -> {}
        }
    }
    Column {
        Text(
            text = "Hello ${viewModel.name}",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(10.dp)
        )
        Card {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .clickable { buttonIsCliked = "gender" }) {
                Icon(
                    painterResource(id = R.drawable.person_24px),
                    contentDescription = "",
                    Modifier.size(38.dp)
                )
                Text(
                    text = viewModel.gender,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .clickable { buttonIsCliked = "height" }) {
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
                .clickable { buttonIsCliked = "weight" }) {
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
                .clickable { buttonIsCliked = "birthdayDate" }) {
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun genderAlertDialog(Gender: String, onDismis: (String) -> Unit) {

    AlertDialog(onDismissRequest = { onDismis(Gender) }) {
        Card {
            Column (
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(20.dp), verticalArrangement = Arrangement.SpaceAround){
                Text(text = stringResource(R.string.select_gender), style = MaterialTheme.typography.displayMedium, color = Color.White)
                Text(text = stringResource(R.string.male),
                    Modifier
                        .clickable { onDismis("Male") }
                        .fillMaxWidth(), style = MaterialTheme.typography.displayMedium, color = Color.White)
                Text(text = stringResource(R.string.female),
                    Modifier
                        .clickable { onDismis("Female") }
                        .fillMaxWidth(), style = MaterialTheme.typography.displayMedium, color = Color.White)
            }
        }
    }
}

