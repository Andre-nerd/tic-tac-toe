package ru.tic_tac_toe.zoomparty.data.service

import android.bluetooth.BluetoothDevice
import ru.tic_tac_toe.zoomparty.data.service.master.MasterBluetoothService
import ru.tic_tac_toe.zoomparty.data.service.slave.SlaveBluetoothService
import ru.tic_tac_toe.zoomparty.domain.BaseService
import ru.tic_tac_toe.zoomparty.domain.WrapperDataContainer
import javax.inject.Inject

class RemoteServiceProvider @Inject constructor(private val dataContainer: WrapperDataContainer) {
    fun getMasterService(): BaseService {
        return MasterBluetoothService(dataContainer)
    }
    fun getSlaveService(device: BluetoothDevice): BaseService {
        val service =  SlaveBluetoothService(dataContainer)
        service.setDevice(device)
        return service
    }
}