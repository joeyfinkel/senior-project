package writenow.app.screens.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import writenow.app.components.post.PostBox
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.PostState
import writenow.app.state.UserState

@Composable
fun NewPost(navController: NavController) {
    val (value, setValue) = remember { mutableStateOf("") }

    fun post() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = JSONObject().apply {
                put("userID", UserState.id)
                put("postContents", value)
                put("visible", 1)
                put("private", if (PostState.isPrivate) 0 else 1)
            }

            Posts.post(json) {
                if (it) UserState.hasPosted = true
            }
            PostState.updateAll()
        }
    }

    PostBox(
        navController = navController,
        placeholder = "New post",
        onBtnClick = {
            if (!UserState.hasPosted && value.isNotBlank()) {
                post()
                navController.navigate(Screens.Posts)
            }
        },
        onValueChange = { setValue(it) },
        editing = false,
        questionOfTheDay = UserState.currentQuestion?.text
    ) { PostState.isPrivate = it }
}