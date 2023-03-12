package writenow.app.components.bottom.bar

import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
internal fun BaseIcon(
    navController: NavController,
    defaultIcon: ImageVector,
    selectedIcon: ImageVector,
    screen: String
) {
    val currentScreen by remember { mutableStateOf(navController.currentDestination?.route) }
    var selected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (currentScreen == screen) selected = true
    }

    IconButton(
        onClick = {
            if (currentScreen != screen) navController.navigate(screen)
        }
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else defaultIcon,
            contentDescription = screen,
            modifier = Modifier.size(25.dp),
            tint = if (selected) Color.White else Color.Unspecified
        )
    }
}