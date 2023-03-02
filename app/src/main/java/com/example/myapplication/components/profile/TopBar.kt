package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.More
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TopBar(
    title: String,
    hasEllipsis: Boolean = false,
    state: ModalBottomSheetState? = null,
    coroutineScope: CoroutineScope? = null,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
        }
        Text(text = title)
        if (hasEllipsis) More { coroutineScope?.launch { state?.show() } }
        else Box(
            modifier = Modifier.width(
                40.dp
            ), content = {})
    }
}