package i.tic_tac_toe.kotlin.ui.widgets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowScreenRationalePermission(permissionsState:MultiplePermissionsState){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(36.dp)) {
            val textToShow = if (permissionsState.shouldShowRationale) {
                "Обмен данными между мобильным приложением и сервером абонентского терминала осуществляется с помощью каналов  радиосвязи Bluetooth или Wi-Fi."
            } else {
                "Обмен данными между мобильным приложением и сервером абонентского терминала осуществляется с помощью каналов  радиосвязи Bluetooth или Wi-Fi."
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Text(textToShow)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Text("Для корректной работы приложения необходимо предоставить пользовательские разрешения.")
            }
            Spacer(modifier = Modifier.size(40.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = {
            permissionsState.launchMultiplePermissionRequest()
                }) {
                    Text("Предоставить разрешения")
                }
            }
        }
    }
}