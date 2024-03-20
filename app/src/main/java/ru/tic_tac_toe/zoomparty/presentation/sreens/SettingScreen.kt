package ru.tic_tac_toe.zoomparty.presentation.sreens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.R
import ru.tic_tac_toe.zoomparty.domain.Configuration
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import ru.tic_tac_toe.zoomparty.presentation.ServiceViewModel
import ru.tic_tac_toe.zoomparty.presentation.navigation.Route
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeLarge
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.fontSizeSmall
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.largePadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.nPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.nUiPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.sUiPadding
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleAboutText
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleAboutTextBold
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleLargeText
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.supPadding


@Composable
fun SettingScreen(serviceViewModel: ServiceViewModel, navController: NavHostController) {
    val context = LocalContext.current
    serviceViewModel.readSettingToSharedPref()
    var isMasterProfile by remember { mutableStateOf(Configuration.profileDevice == WorkProfile.MASTER) }
    var textButton by remember { mutableStateOf(if (isMasterProfile) "Начать сессию" else "подключиться к игре") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sUiPadding.dp, end = sUiPadding.dp, top = nUiPadding.dp, bottom = nUiPadding.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectMasterSlave(callback = { value ->
            isMasterProfile = value
            textButton = if (isMasterProfile) "Начать сессию" else "подключиться к игре"
        })
        Spacer(modifier = Modifier.size(largePadding.dp))
        if (isMasterProfile.not()) SelectBTDevice()
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val device = Configuration.getSelectedDevice()
                val workProfile = if (isMasterProfile) WorkProfile.MASTER else WorkProfile.SLAVE
                try {
                    serviceViewModel.connectionWithRemoteService(workProfile, device)
                } catch (t: Throwable) {
                    Toast.makeText(context, "Ошибка подключения к Bluetooth устройству", Toast.LENGTH_SHORT).show()
                }
                navController.navigate(Route.Game.name)
                serviceViewModel.saveSettingToSharedPref(workProfile, device)
            },
            modifier = Modifier.padding(bottom = sUiPadding.dp)
        ) {
            Text(text = textButton)
        }
    }
}

@Composable
fun SelectMasterSlave(callback: (Boolean) -> Unit) {
    val radioOptions = listOf(WorkProfile.MASTER.mName, WorkProfile.SLAVE.mName)
    val selected = if (Configuration.profileDevice == WorkProfile.MASTER) 0 else 1
    var selectedOption by remember { mutableStateOf(radioOptions[selected]) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Это устройство выступит как: ", style = styleLargeText)
        Spacer(modifier = Modifier.size(sUiPadding.dp))
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            radioOptions.forEach { choiceName ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        modifier = Modifier.size(supPadding.dp),
                        selected = (choiceName == selectedOption),
                        onClick = {
                            selectedOption = choiceName
                            val isMasterProfile = choiceName == WorkProfile.MASTER.mName
                            callback.invoke(isMasterProfile)
                        }
                    )
                    Text(
                        text = choiceName,
                        fontSize = fontSizeLarge.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = nUiPadding.dp)
                    )
                    Spacer(modifier = Modifier.size(sUiPadding.dp))
                }
            }
        }

    }
}

@SuppressLint("MissingPermission")
@Composable
fun SelectBTDevice() {
    var nameDevice: String = "no name"
    var macDevice = "00:00:00:00:00:00"
    val lastDevice = Configuration.getSelectedDevice()
    if (lastDevice != null) {
        nameDevice = lastDevice.name
        macDevice = lastDevice.address
    }
    val fullName = "$nameDevice : $macDevice"

    var openSelectBtDevice by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf(nameDevice) }

//    val nameDevice = bluetoothDevice?.name ?: "no name"
//    val macDevice = bluetoothDevice?.address ?: "00:00:00:00:00:00"
//    val fullName  = "$nameDevice : $macDevice"

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Подключиться к мастер-устройству:", style = styleAboutTextBold)
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = fullName, style = styleAboutText)
        IconButton(onClick = { openSelectBtDevice = !openSelectBtDevice }) {
            Icon(Icons.Filled.List, contentDescription = "Select")
        }
    }
    if (openSelectBtDevice) {
        var nSelected = Configuration.getIndexSelectedDevice()
        if (nSelected == -1) nSelected = 0
        DialogSelectBTDevice(
            title = "Cписок мастер-устройств",
            subTitle = null,
            radioOptions = Configuration.boundedDevices,
            nSelectedElement = nSelected,
            onDismissRequest = { openSelectBtDevice = !openSelectBtDevice },
            onConfirmation = { device ->
                selectedDevice = device.name
                openSelectBtDevice = !openSelectBtDevice
                Configuration.setLastDevice(device)
            }
        )
    }
}

@SuppressLint("MissingPermission")
@Composable // Принимает на вход список DeviceItem
fun DialogSelectBTDevice(
    title: String?,
    subTitle: String?,
    radioOptions: List<BluetoothDevice>,
    nSelectedElement: Int,
    onDismissRequest: () -> Unit,
    onConfirmation: (BluetoothDevice) -> Unit,
) {
    if (radioOptions.isEmpty()) {
        Toast.makeText(LocalContext.current, "Доверенных устройств не найдено. Проверьте подключение bluetooth", Toast.LENGTH_LONG).show()
        return
    }
    val selected = if (nSelectedElement > radioOptions.size - 1) 0 else nSelectedElement
    var selectedOption by remember { mutableStateOf(radioOptions[selected]) }

    val cHeight = getHeightDialogWindow(radioOptions.size)

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cHeight.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(nPadding.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                if (title != null) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(sUiPadding.dp),
                        text = title,
                        fontSize = fontSizeSmall.sp
                    )
                }
                if (subTitle != null) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = subTitle,
                        fontSize = fontSizeSmall.sp
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                radioOptions.forEach { choiceName ->
                    val nameDevice = choiceName.name ?: "no name"
                    val macDevice = choiceName.address ?: "00:00:00:00:00:00"
                    val fullName = "$nameDevice : $macDevice"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (choiceName == selectedOption),
                            onClick = { selectedOption = choiceName }
                        )
                        Text(
                            text = fullName,
                            fontSize = fontSizeSmall.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
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
                        Text(stringResource(id = R.string.dismiss_dialog))
                    }
                    TextButton(
                        onClick = { onConfirmation(selectedOption) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.confirm_dialog))
                    }
                }
            }

        }
    }
}

fun getHeightDialogWindow(count: Int) = 225 + count * 40