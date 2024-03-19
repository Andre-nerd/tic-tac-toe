package ru.tic_tac_toe.zoomparty.domain

import ru.tic_tac_toe.zoomparty.data.service.master.MasterBluetoothService
import ru.tic_tac_toe.zoomparty.data.service.slave.SlaveBluetoothService
import javax.inject.Inject

class RemoteServiceProvider @Inject constructor(private val dataContainer: WrapperDataContainer) {
    fun getMasterService(): BaseService {
        return MasterBluetoothService(dataContainer)
    }
    fun getSlaveService(): BaseService {
        return SlaveBluetoothService(dataContainer)
    }
}