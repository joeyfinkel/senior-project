package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplication.components.TopBar
import com.example.myapplication.components.bottomoverlay.BottomOverlay
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    hasEllipsis: Boolean = false,
    onBackClick: () -> Unit,
    content: @Composable ((PaddingValues, state: ModalBottomSheetState, scope: CoroutineScope) -> Unit)
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                hasEllipsis = hasEllipsis,
                onBackClick = onBackClick,
                state = sheetState,
                coroutineScope = scope
            )
        },
        content = { innerPadding ->
            BottomOverlay(
                sheetContent = { /*TODO*/ },
                maxHeight = 0.5,
                sheetState = sheetState,
                content = { content(innerPadding, sheetState, scope) }
            )
        }
    )
}