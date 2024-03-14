package com.example.coursework.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


//карточка которая используется при выборе скролом
@Composable
fun ScrollCard(n: Int, h: Dp, isMiddle: Boolean = false) {
    if (n >=0) {
        Box(
            modifier = Modifier
                .height(h)
                .width(100.dp), contentAlignment = Alignment.Center
        ) {
            if (isMiddle) {
                Text(
                    text = "$n",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "$n",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .height(h)
                .width(100.dp)
        ) {}
    }
}