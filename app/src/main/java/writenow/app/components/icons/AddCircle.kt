package writenow.app.components.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun BaseAddCircle(size: Dp) {
    Icon(
        Icons.Outlined.Add,
        contentDescription = "Test",
        modifier = Modifier.size(size),
        tint = Color.Black
    )
}

@Composable
fun AddCircle(modifier: Modifier = Modifier, size: Dp = 50.dp, onClick: (() -> Unit)? = null) {
    BaseCircle(modifier, size = size, icon = { BaseAddCircle(size / 2) }, onClick = onClick)
}