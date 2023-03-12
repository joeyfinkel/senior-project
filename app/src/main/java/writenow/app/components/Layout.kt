package writenow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.components.bottom.overlay.comments.Comments
import writenow.app.components.bottombar.BottomBar
import writenow.app.components.bottomoverlay.BottomOverlay
import writenow.app.components.icons.AccountCircle
import writenow.app.components.icons.AddCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultRadius
import writenow.app.ui.theme.Primary

@Composable
private fun TopBar(
    title: String,
    navController: NavController,
    lazyListState: LazyListState,
    scope: CoroutineScope
) = Surface(
    color = Primary,
    shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
) {
    SmallTopAppBar(
        title = {
            ClickableText(
                text = AnnotatedString(title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                onClick = { scope.launch { lazyListState.scrollToItem(0) } }
            )
        },
        actions = {
            AccountCircle(size = 50.dp) {
                SelectedUserState.username = UserState.username
                navController.navigate(Screens.UserProfile)
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Primary
        ),
        // disable user interaction with the top bar
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun Layout(
    navController: NavController,
    topBar: @Composable (scope: CoroutineScope) -> Unit,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val currentDestination = navController.currentDestination?.route


    var maxHeight = 1.0

    if (UserState.isEllipsisClicked) maxHeight = 0.35
    else if (UserState.isCommentClicked) maxHeight = 0.5

    Scaffold(
        topBar = { topBar(scope) },
        content = { innerPadding ->
            BottomOverlay(
                sheetContent = {
                    if (UserState.isCommentClicked) {
                        Comments(navController)
                    } else if (UserState.isEllipsisClicked) {
                        LazyColumn {
                            items(50) {
                                ListItem { Text("Item $it") }
                            }
                        }
                    }
                },
                maxHeight = maxHeight,
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
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
            }
        },
        floatingActionButton = { if (currentDestination == Screens.Posts) floatingActionButton() },
        bottomBar = {
            if (!sheetState.isVisible) {
                BottomBar(navController)
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    title: String,
    navController: NavController,
    lazyListState: LazyListState,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = Layout(
    navController = navController,
    topBar = {
        TopBar(
            title = title,
            navController = navController,
            lazyListState = lazyListState,
            scope = it
        )
    },
    floatingActionButton = {
        AddCircle(size = 50.dp) {
            navController.navigate(Screens.NewPost)
        }
    },
    content = content
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    navController: NavController,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = Layout(
    navController = navController,
    topBar = { },
    floatingActionButton = {
        AddCircle(size = 50.dp) {
            navController.navigate(Screens.NewPost)
        }
    },
    content = content
)