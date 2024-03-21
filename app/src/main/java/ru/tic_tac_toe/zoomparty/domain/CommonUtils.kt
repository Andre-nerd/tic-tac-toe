package ru.tic_tac_toe.zoomparty.domain

import android.util.Log
import ru.tic_tac_toe.zoomparty.domain.Configuration.DRAW_LOG_TAG
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun getFloatFromByteArray(array: ByteArray): Float{
    return try {
        ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).float
    } catch (t: Throwable) {
        Log.e(DRAW_LOG_TAG, "Error getFloatFromByteArray()")
        0f
    }
}

fun Float.getByteArrayFromFloat():ByteArray {
    return ByteBuffer.allocate(4).putFloat(this).array()
}