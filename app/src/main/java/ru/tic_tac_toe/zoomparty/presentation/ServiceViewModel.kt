package ru.tic_tac_toe.zoomparty.presentation

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.tic_tac_toe.zoomparty.App
import ru.tic_tac_toe.zoomparty.domain.BaseService
import ru.tic_tac_toe.zoomparty.data.service.RemoteServiceProvider
import ru.tic_tac_toe.zoomparty.domain.Configuration
import ru.tic_tac_toe.zoomparty.domain.Configuration.SHARED_PREF
import ru.tic_tac_toe.zoomparty.domain.WorkProfile
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val serviceProvider: RemoteServiceProvider
) : ViewModel() {
    private var remoteService: BaseService? = null
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

    @SuppressLint("MissingPermission")
    fun getBoundedDevices(): Set<BluetoothDevice> {
        return App.bluetoothAdapter?.bondedDevices ?: emptySet()
    }

    fun connectionWithRemoteService(profile: WorkProfile, device: BluetoothDevice?) {
        remoteService = if (profile == WorkProfile.MASTER) {
            serviceProvider.getMasterService()
        } else {
            if (device != null) serviceProvider.getSlaveService(device) else throw IOException("Error fun connectionWithRemoteService | bluetooth device  = null")
        }
        remoteService!!.start()
        connectWithRemoteService(device)
    }

    private fun connectWithRemoteService(device: BluetoothDevice?) {
        viewModelScope.launch {
            try{
                remoteService?.openConnection(device)
            } catch(e:Throwable){
                Log.e(Configuration.BT_LOG_TAG, "ERROR ServiceViewModel | fun connectWithRemoteService | remoteService?.openConnection(device):$e")
            }

        }
    }

    fun sendData(byteArray: ByteArray) {
        viewModelScope.launch {
            remoteService?.sendData(byteArray)
        }
    }

    fun saveSettingToSharedPref(mode: WorkProfile, device: BluetoothDevice?) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_MODE_DEVICE, mode.mName)
        editor.putString(KEY_ADDRESS_DEVICE, device?.address ?: "")
        editor.apply()
    }

    fun readSettingToSharedPref() {
        val profileName = sharedPreferences.getString(KEY_MODE_DEVICE, WorkProfile.MASTER.mName) ?: WorkProfile.MASTER.mName
        Configuration.findProfileByName(profileName)
        val address = sharedPreferences.getString(KEY_ADDRESS_DEVICE, "00:00:00:00:00:00") ?: "00:00:00:00:00:00"
        Configuration.setLastDevice(address = address)
    }


    override fun onCleared() {
        super.onCleared()
        remoteService?.closeConnection()
        remoteService = null
    }

    companion object {
        const val KEY_MODE_DEVICE = "MODE_DEVICE"
        const val KEY_ADDRESS_DEVICE = "ADDRESS_DEVICE"
    }
}
