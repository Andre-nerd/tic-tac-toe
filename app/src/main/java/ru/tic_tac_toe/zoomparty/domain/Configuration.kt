package ru.tic_tac_toe.zoomparty.domain

import android.bluetooth.BluetoothDevice

object Configuration {
    const val BT_LOG_TAG = "BT_LOG_TAG"
    const val F_BUFFER = 1
    const val DATA_BUFFER = 9
    const val F_BUFFER_VALUE = 36.toByte()

    // Mode device MASTER or SLAVE
    var profileDevice: WorkProfile = WorkProfile.MASTER
        private set
    fun setProfileDevice(profile: WorkProfile) {
        profileDevice = profile
    }

    // Last BT device
    var lDeviceMacAddress:String? = null
        private set
    fun setLastDevice(device: BluetoothDevice) {
        lDeviceMacAddress = device.address
    }

    //List bounded devices
    var boundedDevices: MutableList<BluetoothDevice> = mutableListOf()
        private  set
    fun setBoundedDevices(set:Set<BluetoothDevice>){
        boundedDevices.addAll(set)
    }
    fun getIndexSelectedDevice():Int{
        boundedDevices.forEachIndexed { index, value ->
            if (value.address == lDeviceMacAddress) return index
        }
        return -1
    }
    fun getSelectedDevice():BluetoothDevice? {
        return boundedDevices.find { it.address == lDeviceMacAddress }
    }

}
