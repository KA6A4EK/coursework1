package com.example.coursework.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


//иконка для активностей
@Composable
fun IconForActivities(
    modifier: Modifier = Modifier,
    drawable: Int,// id иконки
    description: String,//описание активности
    color: Color = Color(93, 151, 87, 170),
) {
    Column(modifier = Modifier.width(80.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .size(70.dp)
                .background(color)
                .padding(bottom = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(id = drawable),
                contentDescription = description,
                modifier = Modifier
                    .size(40.dp),
                Color.White
            )
        }
        Text(
            text = truncateText(description, 9),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


fun truncateText(description: String, maxLength: Int): String {
    return if (description.length > maxLength) {
        description.substring(0, maxLength) + "..."
    } else {
        description
    }
}