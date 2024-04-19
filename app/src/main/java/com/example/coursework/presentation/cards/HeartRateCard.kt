package com.example.coursework.presentation.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.coursework.R
import com.example.coursework.presentation.ViewM.HealthViewModel


//карточка пульса
@Composable
fun CardHeartRate(viewModel: HealthViewModel, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${viewModel.currentDay.heartRateList.lastOrNull()?.value?: stringResource(R.string.heartRateListvalueisNull)} ${stringResource(R.string.bpm)}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}