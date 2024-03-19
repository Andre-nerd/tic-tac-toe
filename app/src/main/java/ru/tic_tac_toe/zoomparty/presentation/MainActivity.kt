package ru.tic_tac_toe.zoomparty.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import i.tic_tac_toe.kotlin.ui.widgets.ShowScreenRationalePermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.tic_tac_toe.zoomparty.R
import ru.tic_tac_toe.zoomparty.presentation.StateHolder.remoteService
import ru.tic_tac_toe.zoomparty.data.service.BaseService
import ru.tic_tac_toe.zoomparty.data.service.master.MasterBluetoothService
import ru.tic_tac_toe.zoomparty.data.service.slave.SlaveBluetoothService
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.CianDark
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.CianLight
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.Tic_tac_toeTheme
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.styleAboutText
import ru.tic_tac_toe.zoomparty.presentation.ui.widgets.DialogSelectOptionRadioGroup
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    private val pushOnBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
        }
    }
    @Inject lateinit var masterService: MasterBluetoothService
    @Inject lateinit var slaveService: SlaveBluetoothService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    private fun FeatureThatRequiresPermissions() {

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
                MainScreen(masterService,slaveService)
                IndicatorBalls()
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



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(masterService: BaseService, slaveService: BaseService) {
    val showBoxMessage = StateHolder.messageWasReceive.collectAsState()
    Column {

        MasterOrSlave(masterService,slaveService)
        AnimatedMessageBox(showBoxMessage.value)

    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MasterOrSlave(masterService: BaseService, slaveService: BaseService){
    var openDialog by remember { mutableStateOf(false) }
    Button(onClick = {openDialog = !openDialog}){
      Text(text = "Выбрать роль для этого устройства" )
    }
    Button(onClick = {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            remoteService?.sendData(byteArrayOf(36,99,77,55,22))
        }
    }) {
        Text(text = "Послать сообщение")
    }
    if (openDialog) {
        DialogSelectOptionRadioGroup(
            onDismissRequest = { openDialog = false },
            onConfirmation = { selectedOption ->
                openDialog = false
                remoteService = if(selectedOption == WorkProfile.MASTER) masterService else slaveService
                remoteService!!.start()
                CoroutineScope(Job() +Dispatchers.IO).launch {
                    remoteService!!.openConnection(null)
                }
            }
        )
    }
}

@Composable
fun DisplayRemoteDeviceStatus() {
    val isSocketCreated = StateHolder.isConnectRemoteDevice.collectAsState()
    val socketCreated = if (isSocketCreated.value) "connected" else "disconnected"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,

        ) {
        Text(
            text = "Bluetooth server ",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Remote device: ",
                style = styleAboutText
            )
            Text(
                text = "${StateHolder.remoteDevice?.address ?: "00:00:00:00:00:00"}",
                style = styleAboutText,
                color = CianLight,
            )
        }

        Row() {
            Text(
                text = "Connect: ",
                style = styleAboutText
            )
            Text(
                text = "$socketCreated",
                style = styleAboutText,
                color = if (!isSocketCreated.value) {
                    Color.Red
                } else {
                    CianLight
                }
            )
        }
    }
}

//@Composable
//fun LastMessageDisplay(){
//    val lastMessage  = StateHolder.messageLastReceived.collectAsState()
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 16.dp, end = 16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.Start,
//
//        ) {
//        Text(
//            text = "Last message received: ",
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(top = 16.dp)
//        )
//        Text(
//            text = "${lastMessage.value.toList()}",
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(top = 16.dp)
//        )
//    }
//}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun IndicatorBalls() {
    val isSocketCreated = StateHolder.isConnectRemoteDevice.collectAsState()
    val target = if (isSocketCreated.value) 0f else 200f

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AnimateBall(target, 500, Color.Red)
            AnimateBall(target, 800, CianLight)
            AnimateBall(target, 600, CianDark)
        }
    }
}

@Composable
fun AnimateBall(
    target: Float,
    duration: Int,
    color: Color
) {
    var ballPosition by remember {
        mutableStateOf(Offset(0f, 800f))
    }
    val bounceAnimation = rememberInfiniteTransition(label = "")
    val yAnimation by bounceAnimation.animateFloat(
        initialValue = 200f, targetValue = target, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    ballPosition = Offset(ballPosition.x, yAnimation)


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(50.dp)
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Canvas(modifier = Modifier.size(50.dp)) {
            drawCircle(
                color = color,
                radius = 50f,
                center = Offset(ballPosition.x + 75, ballPosition.y)
            )
        }
    }
}

@Composable
fun AnimateLine() {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }

    LaunchedEffect("animationKey") {
        alphaAnimation.animateTo(1f, animationSpec = tween(2500))
        yAnimation.animateTo(100f)
        yAnimation.animateTo(500f, animationSpec = tween(500))
    }
    Canvas(modifier = Modifier.size(50.dp)) {
        drawLine(
            color = Color.Red,
            start = Offset(500f, 500f),
            end = Offset(yAnimation.value, 500f),
            alpha = alphaAnimation.value,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )
    }
}


@Composable
fun AnimatedMessageBox(
    visible: Boolean,
    enter: EnterTransition = fadeIn(animationSpec = tween(durationMillis = 1300, easing = LinearEasing)) + expandHorizontally(expandFrom = Alignment.End),
    exit: ExitTransition = fadeOut(animationSpec = tween(durationMillis = 1300))
            + shrinkHorizontally(),
) {
    Row {
        this@Row.AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit,
        ){
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.box_with_code),
                contentDescription = "",
            )
        }
    }
}