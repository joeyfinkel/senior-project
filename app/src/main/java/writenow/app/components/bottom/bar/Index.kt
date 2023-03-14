package writenow.app.components.bottom.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.screens.Screens
import writenow.app.ui.theme.DefaultRadius
import writenow.app.ui.theme.PersianOrange

@Composable
fun BottomBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = PersianOrange,
                shape = RoundedCornerShape(
                    topEnd = DefaultRadius,
                    topStart = DefaultRadius
                ),
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                screen = Screens.Posts
            )
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Search,
                selectedIcon = Icons.Filled.Search,
                screen = Screens.Search
            )
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Notifications,
                selectedIcon = Icons.Filled.Notifications,
                screen = Screens.Notifications
            )
        }
    }
}