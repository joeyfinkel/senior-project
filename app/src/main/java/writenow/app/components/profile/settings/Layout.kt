package writenow.app.components.profile.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import writenow.app.components.profile.ProfileLayout

//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun SettingsLayout(
//    title: String,
//    onBackClick: () -> Unit,
//    content: @Composable (innerPadding: PaddingValues, state: ModalBottomSheetState, scope: CoroutineScope) -> Unit
//) = ProfileLayout(title = title,
//    onBackClick = onBackClick,
//    content = { innerPadding, state, scope -> content(innerPadding, state, scope) })

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsLayout(
    title: String,
    navController: NavController,
    content: @Composable (innerPadding: PaddingValues, state: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = ProfileLayout(title = title,
    navController = navController,
    hasEllipsis = false,
    onBackClick = { navController.popBackStack() },
    content = { innerPadding, state, scope, _ -> content(innerPadding, state, scope) })