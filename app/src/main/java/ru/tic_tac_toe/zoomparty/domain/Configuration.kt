package ru.tic_tac_toe.zoomparty.domain

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
}
