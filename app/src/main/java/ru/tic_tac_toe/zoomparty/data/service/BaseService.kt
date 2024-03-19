package ru.tic_tac_toe.zoomparty.data.service

import android.bluetooth.BluetoothDevice

interface BaseService {
    suspend fun openConnection(device:BluetoothDevice? = null)
    fun closeConnection()
    suspend fun sendData(data: ByteArray)
    suspend fun receiveData()
    fun start()
}