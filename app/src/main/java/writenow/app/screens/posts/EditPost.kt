package writenow.app.screens.posts

import androidx.compose.runtime.*
import androidx.navigation.NavController
import writenow.app.components.post.PostBox
import writenow.app.dbtables.Posts
import writenow.app.state.UserState

@Composable
fun EditPost(navController: NavController) {
    var value by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var isEdited by remember { mutableStateOf(false) }

    fun edit() {
        Posts.edit(UserState.selectedPost?.id ?: 0, value) {
            if (it) isEdited = true
        }.apply {
            UserState.selectedPost?.isEdited = isEdited

            navController.popBackStack()
        }
    }

    PostBox(navController = navController,
        buttonText = "Edit",
        placeholder = UserState.selectedPost?.text ?: "",
        onBtnClick = { edit() },
        onValueChange = { value = it },
        onChipClick = { isPublic = it })
}