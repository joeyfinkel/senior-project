package writenow.app.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import writenow.app.components.TopBar
import writenow.app.components.bottomoverlay.BottomOverlay
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    navController: NavController,
    hasEllipsis: Boolean = false,
    onBackClick: () -> Unit,
    content: @Composable ((PaddingValues, state: ModalBottomSheetState, scope: CoroutineScope) -> Unit)
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val totalChildren = 2
    val isTheSameUser = SelectedUserState.username == UserState.username

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                hasEllipsis = hasEllipsis,
                onBackClick = onBackClick,
                state = sheetState,
                coroutineScope = scope
            )
        },
        content = { innerPadding ->
            BottomOverlay(
                sheetContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp, vertical = 5.dp)
                            .layoutId("bottomOverlay"),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (isTheSameUser) {
                            BottomOverlayButton(icon = Icons.Default.Settings, text = "Settings") {
                                navController.navigate(Screens.Settings)
                            }
                            LogoutButton(navController = navController, color = Color.Red)
                        }
                    }
                },
                maxHeight = if (isTheSameUser) totalChildren.toFloat() * 0.07 else .3,
                sheetState = sheetState,
                content = { content(innerPadding, sheetState, scope) }
            )
        }
    )
}