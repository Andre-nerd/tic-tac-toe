package ru.tic_tac_toe.zoomparty.presentation

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tic_tac_toe.zoomparty.data.service.BaseService

object StateHolder {

    private const val TIME_SIGNAL_MESSAGE_RECEIVED = 1500L

    private val _isConnectRemoteDevice = MutableStateFlow(false)
    val isConnectRemoteDevice: StateFlow<Boolean>
        get() = _isConnectRemoteDevice

    private val _messageWasReceive = MutableStateFlow(false)
    val messageWasReceive: StateFlow<Boolean>
        get() = _messageWasReceive


    fun putStateIsConnectRemoteDevice(state: Boolean) {
        _isConnectRemoteDevice.value = state
    }

    fun signalThatMessageReceived() {
        _messageWasReceive.value = true
        CoroutineScope(Dispatchers.IO).launch {
            delay(TIME_SIGNAL_MESSAGE_RECEIVED)
            _messageWasReceive.value = false
        }
    }

    var remoteDevice: BluetoothDevice? = null
        private set

    fun putRemoteDeviceToStorage(device: BluetoothDevice) {
        remoteDevice = device
    }

    var remoteService: BaseService? = null
}