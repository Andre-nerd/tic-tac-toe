package ru.tic_tac_toe.zoomparty.presentation.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeSmall
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.nPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.nUiPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.sUiPadding

@Composable
fun DialogError(
    errorName:String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            shape = RoundedCornerShape(sUiPadding.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(sUiPadding.dp),
                    text = "Обнаружена ошибка: $errorName",
                    fontSize = fontSizeSmall.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(nPadding.dp),
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}