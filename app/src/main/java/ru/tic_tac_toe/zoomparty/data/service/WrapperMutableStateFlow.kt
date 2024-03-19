package ru.tic_tac_toe.zoomparty.data.service

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WrapperMutableStateFlow @Inject constructor(){
    private val _messageLastReceived = MutableStateFlow<ByteArray>(byteArrayOf())
    val messageLastReceived: StateFlow<ByteArray>
        get() = _messageLastReceived

    fun putMessageLastReceived(data: ByteArray) {
        Log.i(BT_LOG_TAG, "fun putMessageLastReceived() | Get message ${data.toList()}")
        _messageLastReceived.value = data
    }
}