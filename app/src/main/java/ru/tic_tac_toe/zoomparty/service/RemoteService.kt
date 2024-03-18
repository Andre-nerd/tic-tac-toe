package ru.tic_tac_toe.zoomparty.service

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket

interface RemoteService {
    suspend fun openConnection(device:BluetoothDevice? = null)
    fun closeConnection()
    suspend fun sendData(data: ByteArray)
    suspend fun receiveData()
}