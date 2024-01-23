package com.example.coursework.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Composable
fun SettingsScreen(
    viewModel: HealthViewModel
) {

    var userWeight by remember { mutableIntStateOf(viewModel.userWeight) }
    var userHeight by remember { mutableIntStateOf(viewModel.userHeight) }
    var gender by remember { mutableStateOf(viewModel.gender) }
    var birthday by remember { mutableStateOf(viewModel.birthdayDate) }
    var buttonIsCliked by remember { mutableStateOf("") }
    val dateDialogState = rememberMaterialDialogState()
    if (buttonIsCliked != "") {
        when (buttonIsCliked) {
            "weight" -> SnapToBlockList(num = userWeight, listSize = 500, text = "kg") {
                buttonIsCliked = ""
                userWeight = it
                viewModel.userWeight = it
                viewModel.saveHealthData()
            }

            "height" -> SnapToBlockList(num = userHeight, listSize = 300, text = "cm") {
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

            "birthdayDate" -> {
                dateDialogState.show()
                pickDate(dateDialogState = dateDialogState, edit = viewModel.birthdayDate) {
                    viewModel.birthdayDate = if (it < Date()) {
                        SimpleDateFormat("dd/MM/yyyy").format(it).toString()
                    } else {
                        viewModel.birthdayDate
                    }
                    viewModel.saveHealthData()
                    buttonIsCliked = ""
                }

            }

            "name" -> {
                fillNameAlertDialog(name = viewModel.name) {
                    if (it != "") {
                        viewModel.name = it
                        viewModel.saveHealthData()
                    }
                    buttonIsCliked = ""

                }
            }


            else -> {}
        }
    }
    Column {

        Text(
            text = "Hello ${viewModel.name}",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    buttonIsCliked = "name"
                }
        )
        Card {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
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
                .fillMaxWidth()
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
                .fillMaxWidth()
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
                .fillMaxWidth()
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
                        .clickable { onDismis("Male") }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White)
                Text(text = stringResource(R.string.female),
                    Modifier
                        .clickable { onDismis("Female") }
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White)
            }
        }
    }
}


@Composable
fun pickDate(edit: String, dateDialogState: MaterialDialogState, onClic: (Date) -> Unit) {

    var date by remember { mutableStateOf(Date()) }
    LaunchedEffect(edit) {
        date = SimpleDateFormat("dd/MM/yyyy").parse(edit)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok", onClick = { onClic(date) })

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fillNameAlertDialog(name: String, onDismis: (String) -> Unit) {
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
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Text(
                        text = "Cancel",
                        modifier = Modifier
                            .clickable { onDismis("") },
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "Save",
                        modifier = Modifier
                            .clickable { onDismis(name) },
                        style = MaterialTheme.typography.displaySmall
                    )

                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapToBlockList(num: Int, listSize: Int, text: String, onDismis: (Int) -> Unit) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = num - 1)

    val itemHeight = 50.dp // Замените на высоту вашего элемента в dp
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
    AlertDialog(onDismissRequest = { onDismis(num) }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                LazyColumn(
                    state = scrollState, modifier = Modifier
                        .height(190.dp)
                        .padding(20.dp)
                ) {
                    items(listSize) { index ->
                        if (index == firstIndex + 1) {
                            scrollCard(n = index, h = itemHeight, isMiddle = true)

                        } else {
                            scrollCard(n = index, h = itemHeight)
                        }
                    }
                }
                Column(
                    modifier = Modifier.height(190.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = text, style = MaterialTheme.typography.headlineMedium)
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .clickable { onDismis(num) },
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Save",
                    modifier = Modifier
                        .clickable { onDismis(firstIndex + 1) },
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }


    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scrollState.animateScrollToItem(firstIndex + 1)

        } else {
            scrollState.animateScrollToItem(firstIndex)

        }
    }
}

@Composable
fun scrollCard(n: Int, h: Dp, isMiddle: Boolean = false) {
    Box(
        modifier = Modifier
            .size(h), contentAlignment = Alignment.Center
    ) {
        if (isMiddle) {
            Text(
                text = "$n",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(text = "$n", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
        }


    }
}