package writenow.app.components.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.components.icons.Comment
import writenow.app.components.icons.Like
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectedPost(
    scope: CoroutineScope, sheetState: ModalBottomSheetState, navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                UserState.isPostClicked = false

                scope.launch { sheetState.hide() }
            }) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Close post"
                )
            }
        }
        if (UserState.selectedPost != null) {
            LazyColumn {
                item {
                    PostContent(
                        userId = UserState.selectedPost!!.uuid,
                        username = UserState.selectedPost!!.username,
                        navController = navController,
                        text = UserState.selectedPost!!.text,
                        onClickEnabled = false,
                    )
                }
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Like(
                                postId = UserState.selectedPost!!.id,
                                label = "Like",
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            Comment(
                                label = "Comment",
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                            }

                        }
                        Divider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            startIndent = 10.dp,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
