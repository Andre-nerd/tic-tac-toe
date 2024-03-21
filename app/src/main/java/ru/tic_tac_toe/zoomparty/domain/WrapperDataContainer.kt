package ru.tic_tac_toe.zoomparty.domain

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.tic_tac_toe.zoomparty.domain.Configuration.BT_LOG_TAG
import ru.tic_tac_toe.zoomparty.domain.Configuration.PATH_DATA
import ru.tic_tac_toe.zoomparty.domain.ParserMessages.getListPointsFromMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WrapperDataContainer @Inject constructor(){
    private val _messageLastReceived = MutableStateFlow<ByteArray>(byteArrayOf())
    val messageLastReceived: StateFlow<ByteArray>
        get() = _messageLastReceived
    private val _pointsFromMessage = MutableStateFlow<List<Float>>(emptyList())
    val pointsFromMessage: StateFlow<List<Float>>
        get() = _pointsFromMessage

    fun putMessageLastReceivedToContainer(data: ByteArray) {
        Log.i(BT_LOG_TAG, "fun putMessageLastReceivedToContainer() | Get message ${data.toList()}")
        _messageLastReceived.value = data
        when(data[1]){
            PATH_DATA -> {
                val points  = getListPointsFromMessage(data)
                _pointsFromMessage.value = points

            }
        }
    }

    private val _errorConnect = MutableStateFlow<ErrorConnect>(ErrorConnect.NoError)
    val errorConnect: StateFlow<ErrorConnect>
        get() = _errorConnect

    fun putErrorConnectToContainer(errorConnect: ErrorConnect) {
        Log.i(BT_LOG_TAG, "fun putErrorConnectToContainer() | Get error ${errorConnect.name} ${errorConnect.message}")
        _errorConnect.value = errorConnect
    }
}