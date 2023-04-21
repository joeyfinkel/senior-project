package writenow.app.components.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import writenow.app.components.bottom.overlay.comments.Comments
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ProfileLayout(
    title: String,
    hasEllipsis: Boolean,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopBar(
            title = title,
            hasEllipsis = hasEllipsis,
            onBackClick = onBackClick,
            state = sheetState,
            coroutineScope = scope
        )
    }, content = { innerPadding -> content(innerPadding, sheetState, scope) })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ProfileLayout(
    title: String,
    navController: NavController,
    hasEllipsis: Boolean,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope) -> Unit
) {
    val totalChildren = 2
    val isTheSameUser = SelectedUserState.username == UserState.username

    ProfileLayout(title = title,
        hasEllipsis = hasEllipsis,
        onBackClick = onBackClick,
        content = { innerPadding, state, scope ->
            BottomOverlay(sheetContent = {
                BottomOverlayButtonContainer {
                    when {
                        UserState.isCommentClicked -> Comments(navController = navController)
                        UserState.isEllipsisClicked -> BottomOverlayButtonContainer {
                            BottomOverlayButton(
                                icon = Icons.Default.Settings, text = "Settings"
                            ) {
                                navController.navigate(Screens.Settings)
                            }
                            LogoutButton(navController = navController, color = Color.Red)
                        }
                        isTheSameUser -> {
                            BottomOverlayButton(
                                icon = Icons.Default.Settings, text = "Settings"
                            ) {
                                navController.navigate(Screens.Settings)
                            }
                            LogoutButton(navController = navController, color = Color.Red)
                        }
                    }
                }
            }, maxHeight = when {
                UserState.isCommentClicked -> 0.5
                UserState.isEllipsisClicked -> 0.3
                isTheSameUser -> totalChildren.toFloat() * 0.07
                else -> 0.25
            }, sheetState = state, content = { content(innerPadding, state, scope) })
        })

//    Scaffold(topBar = {
//        TopBar(
//            title = title,
//            hasEllipsis = hasEllipsis,
//            onBackClick = onBackClick,
//            state = sheetState,
//            coroutineScope = scope
//        )
//    }, content = { innerPadding ->
//        BottomOverlay(sheetContent = {
//            BottomOverlayButtonContainer(layoutId = "bottomOverlay") {
//                when {
//                    UserState.isCommentClicked -> Comments(navController = navController)
//                    UserState.isEllipsisClicked -> BottomOverlayButtonContainer(layoutId = "bottomOverlay") {
//                        BottomOverlayButton(icon = Icons.Default.Settings, text = "Settings") {
//                            navController.navigate(Screens.Settings)
//                        }
//                        LogoutButton(navController = navController, color = Color.Red)
//                    }
//                    isTheSameUser -> {
//                        BottomOverlayButton(icon = Icons.Default.Settings, text = "Settings") {
//                            navController.navigate(Screens.Settings)
//                        }
//                        LogoutButton(navController = navController, color = Color.Red)
//                    }
//                }
//            }
//        }, maxHeight = when {
//            UserState.isCommentClicked -> 0.5
//            UserState.isEllipsisClicked -> 0.3
//            isTheSameUser -> totalChildren.toFloat() * 0.07
//            else -> 0.25
//        }, sheetState = sheetState, content = { content(innerPadding, sheetState, scope) })
//    })
}

@Composable
private fun TopSection(
    topText: String,
    topVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
    accountIconAction: (() -> Unit)? = null,
    additionalTopContent: @Composable() (() -> Unit)? = null
) = Column(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    hasBottomSheet: Boolean = true,
    hasEllipsis: Boolean = false,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope) -> Unit
) = ProfileLayout(title = title,
    onBackClick = onBackClick,
    hasEllipsis = hasEllipsis,
    content = { innerPadding, state, scope ->
        if (hasBottomSheet) content(
            innerPadding, state, scope
        )
    })

@OptIn(ExperimentalMaterialApi::class)
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
    content: (LazyListScope.(state: ModalBottomSheetState, scope: CoroutineScope) -> Unit),
    additionalContent: (@Composable () -> Unit)? = null
) = ProfileLayout(
    title = title,
    navController = navController,
    onBackClick = onBackClick,
    hasEllipsis = hasEllipsis,
    content = { innerPadding, state, scope ->
        LazyColumn(contentPadding = innerPadding) {
            if (topText != null) {
                item {
                    TopSection(
                        topText = topText,
                        topVerticalArrangement = topVerticalArrangement,
                        accountIconAction = accountIconAction,
                        additionalTopContent = additionalTopContent
                    )
                }
            }
            content(state, scope)
        }
        additionalContent?.invoke()
    })

@OptIn(ExperimentalMaterialApi::class)
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
    content: (@Composable (PaddingValues, ModalBottomSheetState, CoroutineScope) -> Unit)
) = ProfileLayout(title = title,
    navController = navController,
    onBackClick = onBackClick,
    hasEllipsis = hasEllipsis,
    content = { innerPadding, state, scope ->
        if (topText != null) {
            TopSection(
                topText = topText,
                topVerticalArrangement = topVerticalArrangement,
                accountIconAction = accountIconAction,
                additionalTopContent = additionalTopContent
            )
        }
        content(innerPadding, state, scope)
    })