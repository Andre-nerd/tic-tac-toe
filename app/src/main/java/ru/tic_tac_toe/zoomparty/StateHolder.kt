package ru.tic_tac_toe.zoomparty

import android.bluetooth.BluetoothDevice
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.tic_tac_toe.zoomparty.service.BT_LOG_TAG
import ru.tic_tac_toe.zoomparty.service.BaseService
import ru.tic_tac_toe.zoomparty.service.F_BUFFER_VALUE

object StateHolder {

    private const val TIME_SIGNAL_MESSAGE_RECEIVED = 1500L

    private val _isConnectRemoteDevice = MutableStateFlow(false)
    val isConnectRemoteDevice: StateFlow<Boolean>
        get() = _isConnectRemoteDevice

    private val _messageWasReceive = MutableStateFlow(false)
    val messageWasReceive: StateFlow<Boolean>
        get() = _messageWasReceive

    private val _messageLastReceived = MutableStateFlow<ByteArray>(byteArrayOf())
    val messageLastReceived: StateFlow<ByteArray>
        get() = _messageLastReceived

    fun putStateIsConnectRemoteDevice(state: Boolean) {
        _isConnectRemoteDevice.value = state
    }
    fun putMessageLastReceived(data:ByteArray) {
        Log.i(BT_LOG_TAG, "fun putMessageLastReceived() | Get message ${data.toList()}")
        _messageLastReceived.value = data
    }
    fun signalThatMessageReceived(){
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