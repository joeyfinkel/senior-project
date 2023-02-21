package com.example.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Creates a bottom overlay for the app.
 * @param sheetContent The content of the overlay.
 * @param maxHeight The maximum height of the overlay.
 * @param sheetState The state of the overlay.
 * @param content The content of the main screen of the app.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomOverlay(
    sheetContent: @Composable() (() -> Unit),
    maxHeight: Double,
    sheetState: ModalBottomSheetState,
    content: @Composable() (() -> Unit)
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp * maxHeight).dp)
            ) {
                sheetContent()
            }
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetBackgroundColor = Color.White,
    ) {
        content()
    }
}