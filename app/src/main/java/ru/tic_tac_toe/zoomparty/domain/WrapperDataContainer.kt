package ru.tic_tac_toe.zoomparty.domain

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.tic_tac_toe.zoomparty.domain.Configuration.BT_LOG_TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WrapperDataContainer @Inject constructor(){
    private val _messageLastReceived = MutableStateFlow<ByteArray>(byteArrayOf())
    val messageLastReceived: StateFlow<ByteArray>
        get() = _messageLastReceived

    fun putMessageLastReceivedToContainer(data: ByteArray) {
        Log.i(BT_LOG_TAG, "fun putMessageLastReceivedToContainer() | Get message ${data.toList()}")
        _messageLastReceived.value = data
    }

    private val _errorConnect = MutableStateFlow<ErrorConnect>(ErrorConnect.NoError)
    val errorConnect: StateFlow<ErrorConnect>
        get() = _errorConnect

    fun putErrorConnectToContainer(errorConnect: ErrorConnect) {
        Log.i(BT_LOG_TAG, "fun putErrorConnectToContainer() | Get error ${errorConnect.name} ${errorConnect.message}")
        _errorConnect.value = errorConnect
    }
}