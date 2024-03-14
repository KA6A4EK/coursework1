package com.example.coursework.presentation.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coursework.presentation.ViewM.HealthViewModel
import com.example.coursework.presentation.components.BMIProgress


//карточка индекса массы тела
@Composable
fun CardBMI(onClick: () -> Unit, viewModel: HealthViewModel) {
    val bodyHeight = viewModel.userHeight
    val bodyWeight = viewModel.userWeight
    Card {
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
