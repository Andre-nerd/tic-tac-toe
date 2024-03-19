package ru.tic_tac_toe.zoomparty.data.service.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tic_tac_toe.zoomparty.domain.BaseService
import ru.tic_tac_toe.zoomparty.domain.Configuration.BT_LOG_TAG
import ru.tic_tac_toe.zoomparty.domain.Configuration.DATA_BUFFER
import ru.tic_tac_toe.zoomparty.domain.Configuration.F_BUFFER
import ru.tic_tac_toe.zoomparty.domain.Configuration.F_BUFFER_VALUE

import ru.tic_tac_toe.zoomparty.domain.WrapperDataContainer
import java.io.IOException
import javax.inject.Inject

class MasterBluetoothService @Inject constructor(private val dataContainer: WrapperDataContainer) : Thread(), BaseService {

    private var mmSocket: BluetoothSocket? = null
    private var acceptTread: MasterAcceptThread? = null

    override fun run() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
//        if (mmSocket?.isConnected == true) StateHolder.putStateIsConnectRemoteDevice(true)
        try {
            scope.launch {
                openConnection()
                Log.d(BT_LOG_TAG, "MasterBluetoothService running... fun run() mmSocket = $mmSocket")
                Log.d(BT_LOG_TAG, "MasterBluetoothService running... run() mmSocket.isConnected = ${mmSocket?.isConnected}")
                if (mmSocket?.isConnected == true) {
                    receiveData()
                }
            }
        } catch (e: IOException) {
            Log.d(BT_LOG_TAG, "ConnectedThread error fun getInputData(mmSocket)")
//            StateHolder.putStateIsConnectRemoteDevice(false)
        }
    }

    override suspend fun receiveData() {
        var numBytes: Int
        val fBuffer = ByteArray(F_BUFFER)
        val dataBuffer = ByteArray(DATA_BUFFER)
        while (true) {
            numBytes = try {
                mmSocket?.inputStream?.read(fBuffer)
                if (fBuffer[0] != F_BUFFER_VALUE) {
                    Log.e(BT_LOG_TAG, "MasterBluetoothService | Get first byte != $F_BUFFER_VALUE ${fBuffer[0]} | Continue receive ")
                    continue
                }
                mmSocket?.inputStream?.read(dataBuffer)
                dataContainer.putMessageLastReceived(fBuffer + dataBuffer)
                fBuffer.size + dataBuffer.size
            } catch (e: IOException) {
                Log.d(BT_LOG_TAG, "Input stream was disconnected", e)
                throw e
            }
            Log.d(BT_LOG_TAG, "Server receive numBytes $numBytes")
        }
    }

    override suspend fun openConnection(device: BluetoothDevice?) {
        acceptTread = MasterAcceptThread() { socket ->
            mmSocket = socket
            CoroutineScope(Job() + Dispatchers.IO).launch {
                receiveData()
            }
        }
        acceptTread?.start()
    }

    override suspend fun sendData(data: ByteArray) {
        try {
            Log.e(BT_LOG_TAG, "Server send : ${data.toList()}")
            withContext(Dispatchers.IO) {
                mmSocket?.outputStream?.write(data)
            }
        } catch (e: IOException) {
            Log.e(BT_LOG_TAG, "Error occurred when sending data", e)
            return
        }
    }

    override fun closeConnection() {
        try {
            mmSocket?.close()
            acceptTread?.cancel()
        } catch (e: IOException) {
            Log.e(BT_LOG_TAG, "Could not close the connect socket", e)
        }
    }
}
