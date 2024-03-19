package ru.tic_tac_toe.zoomparty.data.service

import android.graphics.drawable.DrawableContainer
import ru.tic_tac_toe.zoomparty.data.service.master.MasterBluetoothService
import ru.tic_tac_toe.zoomparty.data.service.slave.SlaveBluetoothService
import javax.inject.Inject

class RemoteServiceProvider @Inject constructor(private val dataContainer: WrapperMutableStateFlow) {
    fun getMasterService():BaseService{
        return MasterBluetoothService(dataContainer)
    }
    fun getSlaveService():BaseService{
        return SlaveBluetoothService(dataContainer)
    }
}