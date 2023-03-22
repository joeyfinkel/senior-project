package writenow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.R
import writenow.app.components.bottom.bar.BottomBar
import writenow.app.components.bottom.overlay.BottomOverlay
import writenow.app.components.bottom.overlay.BottomOverlayButtonContainer
import writenow.app.components.bottom.overlay.comments.Comments
import writenow.app.components.icons.AccountCircle
import writenow.app.components.post.SelectedPost
import writenow.app.components.profile.BottomOverlayButton
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultRadius
import writenow.app.ui.theme.PersianOrange

@Composable
private fun TopBar(
    title: String, navController: NavController, lazyListState: LazyListState, scope: CoroutineScope
) = Surface(
    color = PersianOrange,
    shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
) {
    SmallTopAppBar(
        title = {
            ClickableText(text = AnnotatedString(title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onClick = { scope.launch { lazyListState.scrollToItem(0) } })
        },
        actions = {
            AccountCircle(size = 50.dp) {
                SelectedUserState.username = UserState.username
                navController.navigate(Screens.UserProfile)
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = PersianOrange
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun Layout(
    navController: NavController,
    topBar: @Composable (scope: CoroutineScope) -> Unit,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val totalChildren = 2

    var maxHeight = 1.0

    if (UserState.isEllipsisClicked) maxHeight = totalChildren.toFloat() * 0.07
    else if (UserState.isCommentClicked) maxHeight = 0.5
    else if (UserState.isPostClicked) maxHeight = 1.5

    Scaffold(topBar = { topBar(scope) }, bottomBar = {
        BottomBar(navController = navController, modifier = Modifier.zIndex(1f))
    }, contentColor = MaterialTheme.colorScheme.onSurface, content = { innerPadding ->
        BottomOverlay(sheetContent = {
            if (UserState.isCommentClicked) {
                Comments(navController)
            } else if (UserState.isEllipsisClicked) {
                BottomOverlayButtonContainer(layoutId = "postOverlay") {
                    BottomOverlayButton(
                        icon = painterResource(id = R.drawable.report),
                        text = "Report",
                    ) {

                    }
                    BottomOverlayButton(
                        icon = Icons.Default.Delete, text = "Delete", color = Color.Red
                    ) {

                    }
                }
            } else if (UserState.isPostClicked) {
                SelectedPost(
                    scope = scope, sheetState = sheetState, navController = navController
                )
            }
        },
            maxHeight = maxHeight,
            sheetState = sheetState,
            modifier = Modifier.zIndex(2f),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            content(sheetState, scope)
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    title: String,
    navController: NavController,
    lazyListState: LazyListState,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = Layout(
    navController = navController, topBar = {
        TopBar(
            title = title, navController = navController, lazyListState = lazyListState, scope = it
        )
    }, content = content
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    navController: NavController,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = Layout(
    navController = navController, topBar = { }, content = content
)