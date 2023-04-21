package writenow.app.screens.posts

import androidx.compose.runtime.*
import androidx.navigation.NavController
import writenow.app.components.post.PostBox
import writenow.app.dbtables.Posts
import writenow.app.state.UserState

@Composable
fun EditPost(navController: NavController) {
    val (value, setValue) = remember { mutableStateOf("") }
    val (_, setIsPublic) = remember { mutableStateOf(true) }
    val (isEdited, setIsEdited) = remember { mutableStateOf(false) }

    fun edit() {
        Posts.edit(UserState.selectedPost?.id ?: 0, value) {
            if (it) setIsEdited(true)
        }.apply {
            UserState.selectedPost?.isEdited = isEdited

            navController.popBackStack()
        }
    }

    PostBox(navController = navController,
        placeholder = UserState.selectedPost?.text ?: "",
        onBtnClick = { edit() },
        onValueChange = { setValue(it) }, editing = true) { setIsPublic(it) }
}