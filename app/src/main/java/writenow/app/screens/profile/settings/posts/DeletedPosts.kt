package writenow.app.screens.profile.settings.posts

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import writenow.app.components.profile.ProfileLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeletedPosts(navController: NavController) {
    ProfileLayout(
        title = "Deleted Posts",
        navController = navController,
        onBackClick = { navController.popBackStack() },
        content = { _, _ -> }
    )
}