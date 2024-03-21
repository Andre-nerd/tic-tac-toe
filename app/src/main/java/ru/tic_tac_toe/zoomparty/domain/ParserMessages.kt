package ru.tic_tac_toe.zoomparty.domain

import android.util.Log
import androidx.compose.ui.graphics.vector.Path

/**
 *    0      /       1       / 2         /  6               /     10              /  14    /  18       /     22   /  30       /
 * заголовок/   спецификатор /id игрока / начальная точка X / начальная точка Y / offset X  / offset Y / цвет линии   / ширина линии /
 *  byte    /       byte     / int      / float             / float             / float     / float    /     long     / float        /
 *  1 byte  /       1 byte   / 4 byte   /  4 byte           /     4 byte        / 4 byte    / 4 byte   /     8 byte   / 4 byte       /
 */
object ParserMessages {
    fun getListPointsFromMessage(message:ByteArray):List<Float>{
        val posX = getFloatFromByteArray(byteArrayOf(message[6],message[7],message[8],message[9]))
        val posY = getFloatFromByteArray(byteArrayOf(message[10],message[11],message[12],message[13]))
        val dX = getFloatFromByteArray(byteArrayOf(message[14],message[15],message[16],message[17]))
        val dY = getFloatFromByteArray(byteArrayOf(message[18],message[19],message[20],message[21]))
        val res = listOf(posX,posY,dX,dY)
        Log.i(Configuration.DRAW_LOG_TAG, "DrawScreen get new message $res")
        return res
    }

    fun getColorAndWidthLine(message: ByteArray):Pair<ULong,Float>{
        val color = getULongFromByteArray(byteArrayOf(message[22],message[23],message[24],message[25],message[26],message[27],message[28],message[29]))
        val width = getFloatFromByteArray(byteArrayOf(message[30],message[31],message[32],message[33]))
        Log.i(Configuration.DRAW_LOG_TAG, "DrawScreen get new color and width $color |  $width")
        return color to width
    }
}
