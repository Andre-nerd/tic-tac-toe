package ru.tic_tac_toe.zoomparty.presentation.sreens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.tic_tac_toe.zoomparty.domain.Configuration
import ru.tic_tac_toe.zoomparty.domain.ErrorConnect
import ru.tic_tac_toe.zoomparty.domain.WrapperDataContainer
import ru.tic_tac_toe.zoomparty.presentation.ServiceViewModel
import ru.tic_tac_toe.zoomparty.presentation.navigation.Route
import ru.tic_tac_toe.zoomparty.presentation.ui.widgets.DialogError

@Composable
fun GameScreen(serviceViewModel: ServiceViewModel, navController: NavHostController, dataContainer: WrapperDataContainer){
    Log.e(Configuration.BT_LOG_TAG, "openErrorWindow.value | WrapperDataContainer  $dataContainer")
    val openErrorWindow  = dataContainer.errorConnect.collectAsState()
    Column {
        Button(onClick = { navController.navigate(Route.Setting.name) }) {
            Text(text = "Это гейм скрин")
        }
        Button(onClick = {
            serviceViewModel.sendData(byteArrayOf(36, 99, 77, 55, 22))
        }) {
            Text(text = "Послать сообщение")
        }
        Log.e(Configuration.BT_LOG_TAG, "openErrorWindow.value ${openErrorWindow.value.name}")
        if(openErrorWindow.value !is ErrorConnect.NoError){
            Log.e(Configuration.BT_LOG_TAG, "openErrorWindow.value !is ErrorConnect.NoError")
            DialogError(
                errorName = openErrorWindow.value.name,
                onDismissRequest = {dataContainer.putErrorConnectToContainer(ErrorConnect.NoError)},
                onConfirmation = {dataContainer.putErrorConnectToContainer(ErrorConnect.NoError)}
            )
        }
    }
}