package com.example.coursework.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


//иконка для активностей
@Composable
fun IconForActivities(
    drawable: Int,// id иконки
    description: String = "",//описание активности
    onClick: () -> Unit, // действие при клике на иконку
) {
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