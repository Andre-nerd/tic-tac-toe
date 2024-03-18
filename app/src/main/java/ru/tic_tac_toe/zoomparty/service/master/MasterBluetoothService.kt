package ru.tic_tac_toe.zoomparty.service.master

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tic_tac_toe.zoomparty.StateHolder
import ru.tic_tac_toe.zoomparty.service.BT_LOG_TAG
import ru.tic_tac_toe.zoomparty.service.RemoteService
import java.io.IOException

class MasterBluetoothService(
    private val receiveData: (ByteArray) -> Unit,
) : Thread(), RemoteService {

    private var mmSocket: BluetoothSocket? = null

    override fun run() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        Log.d(BT_LOG_TAG, "ConnectedThread fun run() mmSocket = $mmSocket")
        Log.d(BT_LOG_TAG, "ConnectedThread fun run() mmSocket.isConnected = ${mmSocket?.isConnected}")
        if (mmSocket?.isConnected == true) StateHolder.putStateIsConnectRemoteDevice(true)
        try {
            scope.launch {
                receiveData()
            }

        } catch (e: IOException) {
            Log.d(BT_LOG_TAG, "ConnectedThread error fun getInputData(mmSocket)")
            StateHolder.putStateIsConnectRemoteDevice(false)
        }
    }

    override suspend fun receiveData() {
        var numBytes: Int // bytes returned from read()
        val fBuffer: ByteArray = ByteArray(1)
        val fourBuffer: ByteArray = ByteArray(4)

        while (true) {
            Log.d(BT_LOG_TAG, "ConnectedThread fun while (true){")
            // Read from the InputStream.
            numBytes = try {
                mmSocket?.inputStream?.read(fBuffer)
                if (fBuffer[0] != 36.toByte()) {
                    Log.e(BT_LOG_TAG, "Get first byte != 36 ${fBuffer[0]} | Continue receive ")
                    continue
                }
                mmSocket?.inputStream?.read(fourBuffer)
                val lengthData = 14
                Log.i(BT_LOG_TAG, "Get header ${fBuffer[0]} ${fourBuffer.toList()} | lengthData = $lengthData")
                val dBuffer: ByteArray = ByteArray(lengthData.toInt() + 1)
                val countBytes = mmSocket?.inputStream?.read(dBuffer) ?: 0
                receiveData.invoke(fBuffer + fourBuffer + dBuffer)
                Log.i(BT_LOG_TAG, "Get data + crc ${dBuffer.toList()}")
                countBytes
            } catch (e: IOException) {
                Log.d(BT_LOG_TAG, "Input stream was disconnected", e)
                throw e
            }
            Log.d(BT_LOG_TAG, "Server receive numBytes $numBytes")
        }
    }

    override suspend fun openConnection(device: BluetoothDevice?) {
        val acceptTread = MasterAcceptThread() { socket ->
            mmSocket = socket
        }
        acceptTread.start()
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
        } catch (e: IOException) {
            Log.e(BT_LOG_TAG, "Could not close the connect socket", e)
        }
    }
}
