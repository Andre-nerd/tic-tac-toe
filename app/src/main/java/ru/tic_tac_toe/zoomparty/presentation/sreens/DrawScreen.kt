package ru.tic_tac_toe.zoomparty.presentation.sreens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import ru.tic_tac_toe.zoomparty.domain.Configuration
import ru.tic_tac_toe.zoomparty.domain.WrapperDataContainer
import ru.tic_tac_toe.zoomparty.presentation.ui.theme.sUiPadding

@Composable
fun DrawScreen(dataContainer: WrapperDataContainer, callback: (cX: Float, cY: Float, dX: Float, dY: Float) -> Unit) {
    Log.d(Configuration.DRAW_LOG_TAG, "DrawScreen | recompouse")
    val ePath = remember { mutableStateOf(Path()) }
    val tempPath = Path()
    val path = remember { mutableStateOf(Path()) }


    Column {
        var colorState = remember { mutableStateOf(Color.DarkGray)}
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    tempPath.moveTo(change.position.x - dragAmount.x, change.position.y - dragAmount.y)
                    tempPath.lineTo(change.position.x, change.position.y)
                    callback.invoke(change.position.x, change.position.y, dragAmount.x, dragAmount.y)
                    path.value = Path().apply {
                        addPath(tempPath)
                    }
                }
            }) {
            Log.d(Configuration.DRAW_LOG_TAG, "DrawScreen | Canvas ePoints ${ePath.value}")
            drawPath(
                path = path.value,
                color = colorState.value,
                style = Stroke(width = 50f, cap = StrokeCap.Round)
            )
        }
        BottomPanel(callback = { color -> colorState.value = color })
    }
}

@Composable
fun BottomPanel(callback: (Color) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorPalette(callback = callback)
    }
}

@Composable
fun ColorPalette(callback: (Color) -> Unit) {
    val colors = listOf(
        Color.Red,
        Color.Yellow,
        Color.Blue,
        Color.Cyan,
        Color.Magenta,
        Color.Green,
        Color.DarkGray
    )
    LazyRow(modifier = Modifier.padding(sUiPadding.dp)) {
        items(colors) { color ->
            Box(modifier = Modifier
                .padding(sUiPadding.dp)
                .clickable { callback.invoke(color) }
                .size(30.dp)
                .background(color, CircleShape)
            )
        }
    }
}