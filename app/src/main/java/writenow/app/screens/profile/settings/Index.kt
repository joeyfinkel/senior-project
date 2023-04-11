package writenow.app.screens.profile.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.ClickableRow
import writenow.app.components.profile.LogoutButton
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.Section
import writenow.app.screens.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(navController: NavController) {
    ProfileLayout(title = "Settings",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) }) { _, _ ->
        // Account section
        item {
            Spacer(modifier = Modifier.height(25.dp))
            Section(title = "Account") {
                ClickableRow(
                    key = "Account",
                    leadingIcon = Icons.Filled.Person
                ) { navController.navigate(Screens.AccountSettings) }
                ClickableRow(
                    key = "Privacy",
                    leadingIcon = painterResource(id = R.drawable.shield)
                ) { navController.navigate(Screens.Privacy) }
            }
        }
        // Notifications section
        item {
            Spacer(modifier = Modifier.height(25.dp))
            Section(title = "Notifications") {
                ClickableRow(
                    key = "Notifications",
                    leadingIcon = Icons.Filled.Notifications
                ) { navController.navigate(Screens.NotificationsSettings) }
            }
        }
        // Posts section
        item {
            Spacer(modifier = Modifier.height(25.dp))
            Section(title = "Posts") {
                ClickableRow(
                    key = "Deleted posts",
                    leadingIcon = painterResource(id = R.drawable.outline_archive)
                ) { navController.navigate(Screens.DeletedPosts) }
            }
        }
        // Support section
        item {
            Spacer(modifier = Modifier.height(25.dp))
            Section(title = "Support") {
                ClickableRow(
                    key = "Help",
                    leadingIcon = painterResource(id = R.drawable.help)
                ) {}
                ClickableRow(key = "About", leadingIcon = Icons.Filled.Info) {}
                ClickableRow(
                    key = "Report a problem",
                    leadingIcon = painterResource(id = R.drawable.report)
                ) {}
            }
        }
        // Login section
        item {
            Spacer(modifier = Modifier.height(25.dp))
            Section(title = "Login") {
                ClickableRow(
                    key = "Switch account",
                    leadingIcon = painterResource(id = R.drawable.switch_account)
                ) {}
                LogoutButton(navController = navController)
            }
        }
    }
}