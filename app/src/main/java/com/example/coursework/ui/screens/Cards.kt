package com.example.coursework.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.R
import com.example.coursework.ViewM.HealthViewEvent
import com.example.coursework.ViewM.HealthViewModel
import com.example.coursework.model.Training
import kotlin.math.round


@Composable
fun CardSteps(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var buttonIsClicked by remember { mutableStateOf(false) }
    var steps by remember { mutableStateOf(viewModel.currentDay.steps) }
//    LaunchedEffect (viewModel.currentDay.steps){
//        steps = viewModel.currentDay.steps
//    }
    if (buttonIsClicked)
        AlertDialog(
            number = steps,
            title = stringResource(R.string.enter_steps)
        ) {
            if (it > 0) {
                steps = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(steps = it)))
            }
            buttonIsClicked = false
        }

    StartScreenCard(
        showButtonAction = viewModel.permissionForSteps,
        onCardClick = { onCardClick() },
        onButtonClick = { buttonIsClicked = true },
        cardValue = steps,
        target = viewModel.stepsTarget,
        text1 = stringResource(R.string.step),
        text2 = stringResource(id = R.string.enter)
    ) {
        ProgressBar(
            percent = steps / viewModel.stepsTarget.toFloat(),
            barWidth = 10,
            width = 120,
            Color.Green
        )
    }
}

@Composable
fun StartScreenCard(
    onCardClick: () -> Unit,
    onButtonClick: () -> Unit,
    cardValue: Int,
    showButtonAction:Boolean = true,
    target: Int,
    text1: String,
    text2: String,
    composable: @Composable () -> Unit
) {

    Card(modifier = Modifier
        .clickable { onCardClick() }
        .fillMaxWidth()
        .heightIn(100.dp), colors = CardDefaults.cardColors(
        containerColor = if (cardValue >= target) {
            Color(1, 255, 1, 200)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = cardValue.toString(),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    if (showButtonAction){
                    Text(
                        text = "/$target$text1",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray
                    )}
                }
                if (showButtonAction){
                Button(onClick = {
                    onButtonClick()
                }) {
                    Text(text = text2)
                }}
                else{
                    Text(
                    text = "/$target$text1",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )}
            }
            Spacer(modifier = Modifier.weight(1f))
            composable()

        }
    }

}


@Composable
fun CardWater(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var water by remember { mutableStateOf(viewModel.currentDay.water) }
    val cupSize = viewModel.cupSize
    StartScreenCard(
        onCardClick = { onCardClick() },
        onButtonClick = {
            water += cupSize
            viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(water = water)))
        },
        cardValue = water,
        target = viewModel.waterTarget,
        text1 = stringResource(id = R.string.ml),
        text2 = "+${viewModel.cupSize}${stringResource(id = R.string.ml)}"
    ) {
        val image = when (viewModel.currentDay.water) {
            in 0..viewModel.waterTarget / 3 -> R.drawable.water_loss_24px
            in viewModel.waterTarget / 3 + 1..viewModel.waterTarget / 3 * 2 -> R.drawable.water_medium_24px
            else -> R.drawable.water_full_24px
        }
        Image(
            painter = painterResource(id = image),
            contentDescription = stringResource(R.string.water_level),
            modifier = Modifier
                .size(80.dp)
                .padding(5.dp)
        )
    }
}

@Composable
fun CardWaterстраое(
    onCardClick: () -> Unit,
    viewModel: HealthViewModel
) {
    var water by remember { mutableIntStateOf(viewModel.currentDay.water) }
    Card(modifier = Modifier
        .clickable { onCardClick() }
        .fillMaxWidth()
        .heightIn(100.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$water",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/${stringResource(R.string.ml, viewModel.cupSize)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray

                    )
                }
                Button(onClick = {
                    water += viewModel.cupSize
                    viewModel.handleViewEvent(
                        HealthViewEvent.Update(
                            viewModel.currentDay.copy(
                                water = water
                            )
                        )
                    )
                }) {
                    Text(text = "+${stringResource(R.string.ml, viewModel.cupSize)}")
                }
            }
            val image = when (viewModel.currentDay.water) {
                in 0..viewModel.waterTarget / 3 -> R.drawable.water_loss_24px
                in viewModel.waterTarget / 3 + 1..viewModel.waterTarget / 3 * 2 -> R.drawable.water_medium_24px
                else -> R.drawable.water_full_24px
            }
            Image(
                painter = painterResource(id = image),
                contentDescription = stringResource(R.string.water_level),
                modifier = Modifier
                    .size(80.dp)
                    .padding(5.dp)
            )
        }
    }


}

@Composable
fun CardStepsстарое(
    onCardClick: () -> Unit,
    viewModel: HealthViewModel
) {
    var steps by remember { mutableIntStateOf(viewModel.currentDay.steps) }
    var buttonIsClicked by remember { mutableStateOf(false) }
    val stepTarget = viewModel.stepsTarget

    if (buttonIsClicked) {
        AlertDialog(
            number = steps,
            title = stringResource(R.string.enter_steps)
        ) {
            if (it > 0) {
                steps = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(steps = it)))
            }
            buttonIsClicked = false

        }
    }
    Card(modifier = Modifier
        .clickable { onCardClick() }
        .fillMaxWidth()
        .heightIn(100.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(

                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$steps",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.step, stepTarget),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray
                    )
                }

                Button(onClick = {
                    buttonIsClicked = true
                }) {
                    Text(text = stringResource(R.string.enter))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ProgressBar(
                percent = steps / stepTarget.toFloat(),
                barWidth = 10,
                width = 100,
                Color.Green
            )
        }
    }

}


@Composable
fun CardActivity(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var buttonIsClicked by remember { mutableStateOf("") }
    val activities = listOf("Skiing", "Hiking", "Sprint", "Martial")
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
                viewModel.handleViewEvent(HealthViewEvent.InsertTraining(it))
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


@Composable
fun CardEat(viewModel: HealthViewModel, onCardClick: () -> Unit) {
    var cal by remember { mutableStateOf(viewModel.currentDay.eat) }
    var buttonIsClicked by remember { mutableStateOf(false) }
    if (buttonIsClicked) {
        AlertDialog(number = cal, title = stringResource(R.string.enter_calories)) {
            if (it > 0) {
                cal = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(eat = it)))
            }
            buttonIsClicked = false
        }

    }
    StartScreenCard(
        onCardClick = { onCardClick() },
        onButtonClick = {
            buttonIsClicked = true
        },
        cardValue = cal,
        target = viewModel.eatTarget,
        text1 = stringResource(id = R.string.cal),
        text2 = stringResource(id = R.string.enter),
    ) {
        ProgressBar(
            percent = cal / viewModel.eatTarget.toFloat(),
            barWidth = 10,
            width = 120,
            Color.Yellow
        )
    }
}

@Composable
fun CardEatстарое(
    onCardClick: () -> Unit,
    viewModel: HealthViewModel
) {
    var buttonIsClicked by remember { mutableStateOf(false) }
    val calTarget = viewModel.eatTarget
    var cal by remember { mutableIntStateOf(viewModel.currentDay.eat) }
    if (buttonIsClicked) {
        AlertDialog(number = cal, title = stringResource(R.string.enter_calories)) {
            if (it > 0) {
                cal = it
                viewModel.handleViewEvent(HealthViewEvent.Update(viewModel.currentDay.copy(eat = it)))
            }
            buttonIsClicked = false
        }

    }
    Card(modifier = Modifier
        .clickable { onCardClick() }
        .fillMaxWidth()
        .heightIn(100.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$cal",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.calTarget, calTarget),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray
                    )
                }
                Button(onClick = { buttonIsClicked = true }) {
                    Text(text = stringResource(id = R.string.enter))
                }
            }
        }
    }

}

@Composable
fun CardBMI(onClick: () -> Unit, viewModel: HealthViewModel) {
    val bodyHeight = viewModel.userHeight
    val bodyWeight = viewModel.userWeight
    Card (){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "$bodyHeight",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Text(
                    text = "$bodyWeight",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
            }
            BMIProgress(width = 170, bodyHeight = bodyHeight, bodyWeight = bodyWeight)

        }
    }

}

@Composable
fun BMIProgress(width: Int, bodyHeight: Int, bodyWeight: Int) {
    val minWeight = round(18.5 * bodyHeight * bodyHeight / 10000)
    val maxWeight = round((25 * bodyHeight * bodyHeight / 10000).toDouble())
    val bmi = bodyWeight / bodyHeight.toFloat() / bodyHeight.toFloat() * 10000
    Column {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .width(width.dp)
        ) {
            val w = (size.width / 2 + size.width / 2 * (bmi - 22) / 9)
            val h = when {
                w < 0 -> 0
                w > size.width -> size.width
                else -> w
            }

            drawLine(
                color = Color(255, 186, 8, 255),
                start = Offset(0f, 0f),
                end = Offset(size.width / 3, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(115, 179, 52, 255),
                start = Offset(size.width / 3, 0f),
                end = Offset(size.width / 3 * 2, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Red,
                start = Offset(size.width / 3 * 2, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(h.toFloat() - 25f, -60f)
                lineTo(h.toFloat() + 25f, -60f)
                lineTo(h.toFloat() + 0f, -10f)
                close()
            }
            drawPath(path, color = Color.Blue)
        }
        Row(modifier = Modifier.width(width.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "$minWeight")
            Text(text = "$maxWeight")
        }
    }
}


@Composable
fun ProgressBar(percent: Float, barWidth: Int, width: Int, color: Color) {
    val percent = minOf(1f, percent)
    Box {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .width(width.dp)
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = barWidth.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(size.width * percent, 0f),
                strokeWidth = (barWidth + 5).dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
fun IconForActivities(drawable: Int, description: String = "", onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .background(Color(93, 151, 87, 170), CircleShape), contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(id = drawable), contentDescription = description, modifier = Modifier
                .size(40.dp)
                .clickable { onClick() }, Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogForActivity(type: String, onDismiss: (Training) -> Unit) {
    var duration by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = { }) {
        Card {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = type,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                OutlinedTextField(
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