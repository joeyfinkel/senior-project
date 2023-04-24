package writenow.app.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import writenow.app.R
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
    snackbarWidth: Float? = 1f,
    onBackClick: () -> Unit,
    snackbar: (@Composable (SnackbarData) -> Unit)? = null,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope, SnackbarHostState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            UserState.isEllipsisClicked = false
            UserState.isProfileEllipsisClicked = false
        }
    }

    Scaffold(topBar = {
        TopBar(
            title = title,
            hasEllipsis = hasEllipsis,
            onBackClick = onBackClick,
            state = sheetState,
            coroutineScope = scope
        )
    }, content = { innerPadding ->
        content(
            innerPadding, sheetState, scope, scaffoldState.snackbarHostState
        )
    }, snackbarHost = {
        Box(modifier = Modifier.fillMaxWidth()) {
            SnackbarHost(hostState = scaffoldState.snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(snackbarWidth ?: 1f),
                snackbar = { data -> snackbar?.invoke(data) })
        }
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    navController: NavController,
    hasEllipsis: Boolean,
    snackbarWidth: Float? = 1f,
    onBackClick: () -> Unit,
    snackbar: (@Composable (SnackbarData) -> Unit)? = null,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope, SnackbarHostState) -> Unit
) {
    val totalChildren = 2
    val isTheSameUser = SelectedUserState.id == UserState.id

    ProfileLayout(
        title = title,
        hasEllipsis = hasEllipsis,
        onBackClick = onBackClick,
        snackbarWidth = snackbarWidth,
        snackbar = snackbar,
        content = { innerPadding, state, scope, snackbarState ->
            BottomOverlay(sheetContent = {
                BottomOverlayButtonContainer {
                    when {
                        UserState.isCommentClicked -> Comments(navController = navController)
                        UserState.isEllipsisClicked -> BottomOverlayButtonContainer {
                            BottomOverlayButton(icon = painterResource(id = R.drawable.outline_edit),
                                text = "Edit",
                                onClick = { UserState.editPost(navController, scope, state) })
                            BottomOverlayButton(icon = Icons.Default.Delete,
                                text = "Delete",
                                color = Color.Red,
                                onClick = { })
                        }
                        UserState.isProfileEllipsisClicked -> {
                            BottomOverlayButton(
                                icon = Icons.Default.Settings,
                                text = "Settings",
                                onClick = {
                                    UserState.clickedOnSettings = true

                                    navController.navigate(Screens.Settings)
                                })
                            LogoutButton(navController = navController, color = Color.Red)
                        }
                    }
                }
            },
                maxHeight = when {
                    UserState.isCommentClicked -> 0.5
                    UserState.isProfileEllipsisClicked || UserState.isEllipsisClicked -> totalChildren.toFloat() * 0.07
                    else -> 0.25
                },
                sheetState = state,
                content = { content(innerPadding, state, scope, snackbarState) })
        })
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
    snackbarWidth: Float? = 1f,
    snackbar: (@Composable (SnackbarData) -> Unit)? = null,
    content: @Composable (PaddingValues, ModalBottomSheetState, CoroutineScope, SnackbarHostState) -> Unit
) = ProfileLayout(title = title,
    onBackClick = onBackClick,
    hasEllipsis = hasEllipsis,
    snackbarWidth = snackbarWidth,
    snackbar = snackbar,
    content = { innerPadding, state, scope, snackbarState ->
        if (hasBottomSheet) content(
            innerPadding, state, scope, snackbarState
        )
    })

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileLayout(
    title: String,
    navController: NavController,
    topText: String? = null,
    hasEllipsis: Boolean = false,
    snackbarWidth: Float? = 1f,
    topVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
    onBackClick: () -> Unit,
    accountIconAction: (() -> Unit)? = null,
    additionalTopContent: @Composable (() -> Unit)? = null,
    snackbar: @Composable ((SnackbarData) -> Unit)? = null,
    content: LazyListScope.(ModalBottomSheetState, CoroutineScope, SnackbarHostState) -> Unit
) {
    ProfileLayout(title = title,
        navController = navController,
        hasEllipsis = hasEllipsis,
        onBackClick = onBackClick,
        snackbarWidth = snackbarWidth,
        snackbar = snackbar,
        content = { innerPadding, state, scope, snackbarState ->
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
                content(state, scope, snackbarState)
            }
        })
}

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
    content = { innerPadding, state, scope, _ ->
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