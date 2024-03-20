package ru.tic_tac_toe.zoomparty.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import i.tic_tac_toe.kotlin.ui.widgets.ShowScreenRationalePermission
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.presentation.navigation.NavigateRoute
import ru.tic_tac_toe.zoomparty.presentation.navigation.Route
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.Tic_tac_toeTheme
import ru.tic_tac_toe.zoomparty.presentation.ui.widgets.DialogSelectOptionRadioGroup

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    private val pushOnBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(App.bluetoothAdapter == null){
            Toast.makeText(this,"Bluetooth не доступен на устройстве. Приложение будет закрыто", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            finish()
        }
        setContent {
            Tic_tac_toeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FeatureThatRequiresPermissions()
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun FeatureThatRequiresPermissions(serviceViewModel:ServiceViewModel = viewModel()) {

        val permissionsState = rememberMultiplePermissionsState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.d("PERMISSION_GET", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.S")
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            } else {
                Log.d("PERMISSION_GET", "Build.VERSION.SDK_INT < Build.VERSION_CODES.S")
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            }
        )
        if (permissionsState.allPermissionsGranted) {
            Box(modifier = Modifier.fillMaxWidth()) {
                MainScreen()
            }
        } else {
            ShowScreenRationalePermission(permissionsState)
        }
    }

    private fun openBTActivityForResult() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        pushOnBluetoothLauncher.launch(intent)
    }
}

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    NavigateRoute(navController = navController)
}


//@Composable
//fun SettingScreen(serviceViewModel:ServiceViewModel,navController: NavHostController) {
//    Column {
//        MasterOrSlave(serviceViewModel, navController)
//    }
//}
@Composable
fun SelectWorkProfile(serviceViewModel:ServiceViewModel, navController: NavHostController){

    var openDialog by remember { mutableStateOf(false) }
    Column {
        Button(onClick = { openDialog = !openDialog }) {
            Text(text = "Выбрать роль для этого устройства")
        }
        Button(onClick = {
            serviceViewModel.sendData(byteArrayOf(36, 99, 77, 55, 22))
        }) {
            Text(text = "Послать сообщение")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            navController.navigate(Route.Game.name)
        }) {
            Text(text = "Пропустить")
        }
    }
    if (openDialog) {
        DialogSelectOptionRadioGroup(
            onDismissRequest = { openDialog = false },
            onConfirmation = { workProfile ->
                openDialog = false
                serviceViewModel.connectionWithRemoteService(workProfile, null)
            }
        )
    }
}

@Composable
fun MasterOrSlave(serviceViewModel:ServiceViewModel, navController: NavHostController){

    var openDialog by remember { mutableStateOf(false) }
    Column {
        Button(onClick = { openDialog = !openDialog }) {
            Text(text = "Выбрать роль для этого устройства")
        }
        Button(onClick = {
            serviceViewModel.sendData(byteArrayOf(36, 99, 77, 55, 22))
        }) {
            Text(text = "Послать сообщение")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            navController.navigate(Route.Game.name)
        }) {
            Text(text = "Пропустить")
        }
    }
    if (openDialog) {
        DialogSelectOptionRadioGroup(
            onDismissRequest = { openDialog = false },
            onConfirmation = { workProfile ->
                openDialog = false
                serviceViewModel.connectionWithRemoteService(workProfile, null)
            }
        )
    }
}
@Composable
fun GameScreen(serviceViewModel:ServiceViewModel, navController: NavHostController){
    Column {
        Button(onClick = { navController.navigate(Route.Setting.name) }) {
            Text(text = "Это гейм скрин")
        }
        Button(onClick = {
            serviceViewModel.sendData(byteArrayOf(36, 99, 77, 55, 22))
        }) {
            Text(text = "Послать сообщение")
        }
    }
}

