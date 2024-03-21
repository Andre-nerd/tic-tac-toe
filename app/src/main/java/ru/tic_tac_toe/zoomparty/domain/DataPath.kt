package ru.tic_tac_toe.zoomparty.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class DataPath(
    val path:Path = Path(),
    val color: ULong = Color.DarkGray.value,
    val lWidth: Float = 10F
)