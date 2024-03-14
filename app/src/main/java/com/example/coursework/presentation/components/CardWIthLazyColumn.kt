package com.example.coursework.presentation.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coursework.R


//карточка для выбора элемента
@Composable
fun CardWithLazyColumnForSelect(
    num: Int,  //начальный элемент
    listSize: Int,  //размер списка для выбора
    text: String,//текст оописание
    pitch: Int,///шаг между элеметами
    initialIndex: Int = 0,  //значение первого элемента если 0 то 0 и его выбрать нельзя если -1 то можно выбрать 0
    onDismiss: (Int) -> Unit
) {
    var ret by remember { mutableIntStateOf(num) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            ScrolableLazyColumn(
                num - 1 / pitch,
                listSize,
                pitch,
                initialIndex = initialIndex
            ) {
                ret = it
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
                text = stringResource(id = R.string.cancel),
                modifier = Modifier
                    .clickable { onDismiss(num * pitch) },
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable { onDismiss(ret) },
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}