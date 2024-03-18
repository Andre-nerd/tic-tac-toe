package ru.tic_tac_toe.zoomparty.service.slave

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.service.BT_LOG_TAG
import ru.tic_tac_toe.zoomparty.service.RemoteService
import java.io.IOException




@SuppressLint("MissingPermission")
class SlaveBluetoothService(
    private val receiveData:(data:ByteArray) -> Unit,
) : Thread(), RemoteService {
    private var device: BluetoothDevice? = null

    fun setDevice(device: BluetoothDevice){
        this.device = device
    }
    private var mmSocket: BluetoothSocket?  = null
    @SuppressLint("MissingPermission")
    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        App.bluetoothAdapter.cancelDiscovery()
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch{
            receiveData()
        }
    }

    override suspend fun receiveData(){
        var numBytes: Int // bytes returned from read()
        val fBuffer: ByteArray = ByteArray(1)
        val fourBuffer: ByteArray = ByteArray(4)
        while (true) {
            Log.d(BT_LOG_TAG, "ConnectedThread fun while (true){")
            // Read from the InputStream.
            numBytes = try {
                mmSocket?.inputStream?.read(fBuffer)
                if (fBuffer[0] != 36.toByte()) {
                    continue
                }
                mmSocket?.inputStream?.read(fourBuffer)
                val lengthData = 14
                val dBuffer: ByteArray = ByteArray(lengthData.toInt() + 1)
                val countBytes =  mmSocket?.inputStream?.read(dBuffer) ?: 0
                receiveData.invoke(fBuffer + fourBuffer + dBuffer)
                countBytes
            } catch (e: IOException) {
                Log.d(BT_LOG_TAG, "Input stream was disconnected", e)
                throw e
            }
        }
    }

    override suspend fun openConnection(device: BluetoothDevice?) {
        if(device == null){
            Log.d(BT_LOG_TAG, "SlaveBindService | device  == null | open connection failed")
            return
        }
        val slaveBindThread = SlaveBindThread(device){ socket ->
            mmSocket = socket
            Log.i(BT_LOG_TAG, "Client | SocketServiceBT | socketThread!!.start()")
            Log.i(BT_LOG_TAG, "Client | SocketServiceBT | socket = $mmSocket")

            if (mmSocket == null){
                Log.i(BT_LOG_TAG, "Client | SocketServiceBT | null socket return false")
            }
            if(mmSocket?.isConnected == true){
                Log.i(BT_LOG_TAG, "Client | SocketServiceBT | mmSocket?.isConnected ${mmSocket?.isConnected}")
            }
        }
        slaveBindThread.start()
    }

    override fun closeConnection() {
        try {
            mmSocket?.close()
            Log.e(BT_LOG_TAG, "Client | ConnectBluetoothThread | mmSocket?.close() success $mmSocket")
        } catch (e: IOException) {
            Log.e(BT_LOG_TAG, "Client | Could not close the client socket", e)
        }
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
}
