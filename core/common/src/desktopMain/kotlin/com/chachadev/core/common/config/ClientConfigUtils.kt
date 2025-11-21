package com.chachadev.core.common.config


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import java.awt.Toolkit

private fun StartWindow.toDpSize() = DpSize(width.dp, height.dp)

private fun ClientConfig.toDpSize() = window?.toDpSize() ?: DpSize.Unspecified

private fun ClientConfig.toPlacement() = if (window == null) WindowPlacement.Fullscreen else WindowPlacement.Floating

private fun ClientConfig.toPosition() = when (val w = window) {
    null -> WindowPosition(Alignment.Center)
    else -> run {
        val screen = Toolkit.getDefaultToolkit().screenSize
        val y = (screen.height - w.height) / 2
        val x = screen.width - y - w.width
        WindowPosition.Absolute(x.dp, y.dp)
    }
}

@Composable
fun ClientConfig.toWindowState() = rememberWindowState(
    position = toPosition(),
    placement = toPlacement(),
    size = toDpSize()
)