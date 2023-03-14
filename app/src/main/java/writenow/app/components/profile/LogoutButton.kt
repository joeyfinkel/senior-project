package writenow.app.components.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.state.UserState

@Composable
fun LogoutButton(
    navController: NavController,
    color: Color = MaterialTheme.colorScheme.onSurface
) = BottomOverlayButton(
    icon = painterResource(id = R.drawable.logout),
    text = "Logout",
    color = color,
    onClick = { UserState.logout(navController) }
)
