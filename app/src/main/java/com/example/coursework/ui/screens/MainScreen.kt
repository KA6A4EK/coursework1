package com.example.coursework.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.coursework.ViewM.HealthViewModel

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HealthViewModel
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: "start"

    Scaffold(
        topBar = { TopAppbar() },
        bottomBar = {
            BottomAppbar(
                homeClick = { if (currentScreen!="start"){navController.navigate("start")} },
                settingsClick = { if (currentScreen!="settings"){navController.navigate("settings")} })
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
                StepsScreen(viewModel )
            }
            composable(route = "water_screen") {
                WaterScreen(viewModel)
            }
            composable(route = "eat_screen") {
                EatScreen(viewModel)
            }
            composable( route = "activity_list"){
                ActivityScreen(viewModel)
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbar() {
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.topAppBarText),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium
        )
    },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
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
                painter = painterResource(id = R.drawable.settings_24px),
                contentDescription = stringResource(R.string.settings_button),
                modifier = Modifier
                    .clickable { settingsClick() }
                    .size(50.dp),
                Color.White
            )
        }
    })
}