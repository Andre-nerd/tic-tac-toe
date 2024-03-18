package ru.tic_tac_toe.zoomparty

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        val bluetoothManager = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    override fun onTerminate() {
        super.onTerminate()
        StateHolder.remoteService?.closeConnection()

    }


    companion object {
        lateinit var appContext: Context
        lateinit var bluetoothAdapter: BluetoothAdapter

        private fun handlerReceivedData(data: ByteArray) {


        }
    }
}