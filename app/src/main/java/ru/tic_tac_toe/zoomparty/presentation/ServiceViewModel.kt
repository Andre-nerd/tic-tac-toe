package ru.tic_tac_toe.zoomparty.presentation

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.data.service.BaseService
import ru.tic_tac_toe.zoomparty.data.service.master.MasterBluetoothService
import ru.tic_tac_toe.zoomparty.data.service.slave.SlaveBluetoothService
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val masterService: MasterBluetoothService,
    private val slaveService: SlaveBluetoothService
) : ViewModel() {
    var remoteService: BaseService? = null

    @SuppressLint("MissingPermission")
    fun getBoundedDevices(): Set<BluetoothDevice> {
        return App.bluetoothAdapter?.bondedDevices ?: emptySet()
    }

    fun setRemoteService(profile: WorkProfile) {
        remoteService = if (profile == WorkProfile.MASTER) {
            masterService as BaseService
        } else {
            slaveService as BaseService
        }
        remoteService!!.start()
    }
    fun connectWithRemoteService(device: BluetoothDevice?){
        viewModelScope.launch {
            remoteService?.openConnection(device)
        }
    }
    fun sendData(byteArray: ByteArray){
        viewModelScope.launch {
            remoteService?.sendData(byteArray)
        }
    }

    override fun onCleared() {
        super.onCleared()
        remoteService?.closeConnection()
        remoteService = null
    }
}
