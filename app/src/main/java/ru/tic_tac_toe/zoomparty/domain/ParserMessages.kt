package ru.tic_tac_toe.zoomparty.domain

import android.util.Log
import androidx.compose.ui.graphics.vector.Path

object ParserMessages {
    fun getListPointsFromMessage(message:ByteArray):List<Float>{
        val posX = getFloatFromByteArray(byteArrayOf(message[2],message[3],message[4],message[5]))
        val posY = getFloatFromByteArray(byteArrayOf(message[6],message[7],message[8],message[9]))
        val dX = getFloatFromByteArray(byteArrayOf(message[10],message[11],message[12],message[13]))
        val dY = getFloatFromByteArray(byteArrayOf(message[14],message[15],message[16],message[17]))
        val res = listOf(posX,posY,dX,dY)
        Log.i(Configuration.DRAW_LOG_TAG, "DrawScreen get new message $res")
        return res
    }
}
//tempPath.moveTo(change.position.x - dragAmount.x, change.position.y - dragAmount.y)
//                tempPath.lineTo(change.position.x, change.position.y)
//                callback.invoke(change.position.x, change.position.y, dragAmount.x, dragAmount.y)
//                path.value = Path().apply {
//                    addPath(tempPath)
//                }