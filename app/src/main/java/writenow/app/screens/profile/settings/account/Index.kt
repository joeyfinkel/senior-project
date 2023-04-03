package writenow.app.screens.profile.settings.account

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import writenow.app.components.profile.ProfileLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountSettings(navController: NavController) {
    ProfileLayout(
        title = "Account",
        navController = navController,
        onBackClick = { navController.popBackStack() },
        content = { _, _ -> })
}