package ru.tic_tac_toe.zoomparty.data.service.slave

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.domain.Configuration.BT_LOG_TAG

import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class SlaveBindThread(
    device: BluetoothDevice,
    private val callback: (socket: BluetoothSocket?) -> Unit
) : Thread() {
    private val uuid = UUID.fromString("e17adf11-0edf-4263-ba1d-f84bd12e5be2")

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(uuid)
    }
    @SuppressLint("MissingPermission")
    override fun run() {
        App.bluetoothAdapter?.cancelDiscovery()

        mmSocket?.let { socket ->
            Log.i(BT_LOG_TAG, "Client | ConnectBluetoothThread | get socket  $socket | try connect...")
            try {
                socket.connect()
                Log.i(BT_LOG_TAG, "Client | ConnectBluetoothThread | socket connect success")
                callback.invoke(socket)
            } catch (e: Throwable) {
                Log.e(BT_LOG_TAG, "Client | ConnectBluetoothThread | Error when socket connect $e")
                callback.invoke(null)
            }
        }
    }

    fun cancel() {
        try {
            mmSocket?.close()
            Log.e(BT_LOG_TAG, "Client | ConnectBluetoothThread | mmSocket?.close() success $mmSocket")
        } catch (e: IOException) {
            Log.e(BT_LOG_TAG, "Client | Could not close the client socket", e)
        }
    }
}
