package writenow.app.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import writenow.app.components.TopBar
import writenow.app.components.bottom.overlay.BottomOverlay
import writenow.app.components.bottom.overlay.BottomOverlayButtonContainer
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    navController: NavController,
    topText: String? = null,
    hasEllipsis: Boolean = false,
    topVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
    onBackClick: () -> Unit,
    accountIconAction: (() -> Unit)? = null,
    additionalTopContent: (@Composable () -> Unit)? = null,
    content: (LazyListScope.(state: ModalBottomSheetState, scope: CoroutineScope) -> Unit)
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val totalChildren = 2
    val isTheSameUser = SelectedUserState.username == UserState.username

    Scaffold(topBar = {
        TopBar(
            title = title,
            hasEllipsis = hasEllipsis,
            onBackClick = onBackClick,
            state = sheetState,
            coroutineScope = scope
        )
    }, content = { innerPadding ->
        BottomOverlay(
            sheetContent = {
                BottomOverlayButtonContainer(layoutId = "bottomOverlay") {
                    if (isTheSameUser) {
                        BottomOverlayButton(icon = Icons.Default.Settings, text = "Settings") {
                            navController.navigate(Screens.Settings)
                        }
                        LogoutButton(navController = navController, color = Color.Red)
                    }
                }
            },
            maxHeight = if (isTheSameUser) totalChildren.toFloat() * 0.07 else .3,
            sheetState = sheetState
        ) {
            LazyColumn(contentPadding = innerPadding) {
                if (topText != null) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = topVerticalArrangement
                        ) {
                            AccountCircle(size = 125.dp,
                                bitmap = if (UserState.id == SelectedUserState.id) UserState.bitmap else null,
                                onClick = { accountIconAction?.invoke() })
                            Text(
                                text = topText, color = MaterialTheme.colorScheme.onSurface
                            )

                            additionalTopContent?.invoke()
                        }

                    }
                }
                content(sheetState, scope)
            }
        }
    })
}