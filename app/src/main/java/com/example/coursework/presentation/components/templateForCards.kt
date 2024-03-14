package com.example.coursework.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

//шаблон для карточек
@Composable
fun StartScreenCard(
    onCardClick: () -> Unit,  //действие при клике на карточку
    onButtonClick: () -> Unit, //дейстие при клике на кнопку
    cardValue: Int,                //значение в карточке
    showButtonAction: Boolean = true,      // показывать ли кнопку в карточке
    target: Int,                                //цель в карточке
    text1: String,
    text2: String,
    composable: @Composable () -> Unit,      //дополнительный элемент в карточке справа для прогресс бара и тд
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
                    if (showButtonAction) {
                        Text(
                            text = "/$target$text1",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                    }
                }
                if (showButtonAction) {
                    Button(onClick = {
                        onButtonClick()
                    }) {
                        Text(text = text2)
                    }
                } else {
                    Text(
                        text = "/$target$text1",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            composable()

        }
    }
}