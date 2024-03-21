package ru.tic_tac_toe.zoomparty.domain

import android.bluetooth.BluetoothDevice

object Configuration {
    const val BT_LOG_TAG = "BT_LOG_TAG"
    const val DRAW_LOG_TAG = "DRAW_LOG_TAG"
    const val F_BUFFER = 1
    const val DATA_BUFFER = 33 // Общая длина массива данных с заголовочным байтом  = 34
    const val F_BUFFER_VALUE = 36.toByte()
    const val PATH_DATA = 1.toByte()
    const val SHARED_PREF = "SHARED_PREF"

    // Mode device MASTER or SLAVE
    var profileDevice: WorkProfile = WorkProfile.MASTER
        private set
    private fun setProfileDevice(profile: WorkProfile) {
        profileDevice = profile
    }
    fun findProfileByName(name:String){
        val profile  = if(name == WorkProfile.SLAVE.mName) WorkProfile.SLAVE else WorkProfile.MASTER
        setProfileDevice(profile = profile)
    }

    // Last BT device
    var lDeviceMacAddress:String? = null
        private set
    fun setLastDevice(device: BluetoothDevice) {
        lDeviceMacAddress = device.address
    }
    fun setLastDevice(address:String) {
        lDeviceMacAddress = address
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
