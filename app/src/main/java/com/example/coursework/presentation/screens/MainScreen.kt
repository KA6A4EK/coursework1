package com.example.coursework.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel


//главный экра тут определяется BottomBar, TopAppBar и навигация
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HealthViewModel,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: "start"
    Scaffold(
        topBar = { TopAppbar(currentScreen, navController) },
        bottomBar = {
            BottomAppbar(
                homeClick = {
                    if (currentScreen != "start") {
                        navController.navigate("start")
                    }
                },
                settingsClick = {
                    if (currentScreen != "settings") {
                        navController.navigate("settings")
                    }
                })
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "start",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = "start") {
                StartScreen(modifier = Modifier, navController, viewModel)
            }
            composable(route = "settings") {
                SettingsScreen(viewModel)
            }
            composable(route = "steps_screen") {
                StepsScreen(viewModel)
            }
            composable(route = "water_screen") {
                WaterScreen(viewModel)
            }
            composable(route = "eat_screen") {
                EatScreen(viewModel)
            }
            composable(route = "activity_list") {
                ActivityScreen(viewModel)
            }
            composable(route = "target/{edit_target}") {
                val curent = backStackEntry?.arguments?.getString("edit_target") ?: "start"
                TargetScreen(viewModel, curent, navigateUp = { navController.navigateUp() })
            }
            composable(route = "edit_start") {
                editStartScreen(viewModel = viewModel)
            }
            composable(route = "notifications_period") {
                SelectNotificationsPeriod(viewModel)
            }
            composable(route = "weight_screen") {
                WeightScreen(viewModel)
            }
            composable(route = "heart_rate_screen") {
                MeasureHeartRateScreen(viewModel)
            }
        }
    }
}

//тут описывается TopAppBar и действия при клике на кнопку в зависимости от текущего экрана
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbar(
    curent: String, //текущий экран
    navController: NavHostController,//обьект отвечающий за навигацию
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    val screens =
        listOf("steps_screen", "activity_list", "eat_screen", "water_screen", "bmi_screen")
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.topAppBarText),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium
        )
    },
        actions = {
            IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }
            if (dropDownMenuExpanded) {
                DropdownMenu(
                    expanded = dropDownMenuExpanded,
                    onDismissRequest = { dropDownMenuExpanded = false },
                    modifier = Modifier.padding(10.dp)
                ) {
                    if (curent == "start") {
                        Text(
                            text = stringResource(R.string.edit_screen),
                            modifier = Modifier.clickable {
                                navController.navigate("edit_start")
                                dropDownMenuExpanded = false
                            },
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(text = "Notifications", modifier = Modifier.clickable {
                            navController.navigate("notifications_period")
                            dropDownMenuExpanded = false
                        }, style = MaterialTheme.typography.headlineLarge)
                    } else if (screens.contains(curent)) {
                        Text(
                            text = stringResource(id = R.string.set_target),
                            modifier = Modifier.clickable {
                                dropDownMenuExpanded = false
                                navController.navigate("target/{$curent}")
                            },
                            style = MaterialTheme.typography.headlineLarge
                        )
                        if (curent == "water_screen") {
                            Text(
                                text = stringResource(id = R.string.set_cup_size),
                                modifier = Modifier.clickable {
                                    dropDownMenuExpanded = false
                                    navController.navigate("target/{cupSize}")
                                },
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }
            }
        })
}


@Composable
fun BottomAppbar(homeClick: () -> Unit, settingsClick: () -> Unit) {
    androidx.compose.material3.BottomAppBar(modifier = Modifier.height(60.dp), actions = {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Icon(
                painter = painterResource(id = R.drawable.home_24px),
                contentDescription = stringResource(R.string.home_button),
                modifier = Modifier
                    .clickable { homeClick() }
                    .size(50.dp),
                Color.White
            )
            Icon(
                painter = painterResource(id = R.drawable.person_24px),
                contentDescription = stringResource(R.string.settings_button),
                modifier = Modifier
                    .clickable { settingsClick() }
                    .size(50.dp),
                Color.White
            )
        }
    })
}