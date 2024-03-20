package ru.tic_tac_toe.zoomparty.presentation.ui.widgets

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeSmall

const val DIALOG_LOG = "DIALOG_LOG"

@Composable
fun DialogSelectOptionRadioGroup(
    onDismissRequest: () -> Unit,
    onConfirmation: (WorkProfile) -> Unit,
) {
    val radioOptions = listOf("Мастер", "Игрок")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
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
                        .padding(16.dp),
                    text = "Этот девайс будет выступать как:",
                    fontSize = fontSizeSmall.sp
                )
                Row {
                    radioOptions.forEach { choiceName ->
                        Column() {
                            RadioButton(
                                selected = (choiceName == selectedOption),
                                onClick = { selectedOption = choiceName }
                            )
                            Text(
                                text = choiceName,
                                fontSize = fontSizeSmall.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = { onConfirmation(if(selectedOption == WorkProfile.MASTER.mName) WorkProfile.MASTER else WorkProfile.SLAVE) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Выбрать")
                    }
                }
            }
        }
    }
}