package writenow.app.screens.profile.settings.account

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import writenow.app.components.profile.ProfileLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Privacy(navController: NavController) {
    ProfileLayout(
        title = "Privacy",
        navController = navController,
        snackbar = null,
        onBackClick = { navController.popBackStack() },
        content = { _, _, _ -> })
}