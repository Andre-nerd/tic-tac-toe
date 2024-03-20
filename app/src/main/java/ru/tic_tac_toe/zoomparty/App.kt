package ru.tic_tac_toe.zoomparty

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import ru.tic_tac_toe.zoomparty.domain.Configuration

@HiltAndroidApp
class App : Application() {
    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        try {
            bluetoothAdapter = bluetoothManager.adapter
            Configuration.setBoundedDevices(bluetoothAdapter!!.bondedDevices)
        } catch (t:Throwable){
            Log.e(Configuration.BT_LOG_TAG, "Bluetooth adapter is not available")
        }
    }

    companion object {
        var bluetoothAdapter: BluetoothAdapter? = null
    }
}