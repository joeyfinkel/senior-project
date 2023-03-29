package writenow.app.utils

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.dbtables.Post
import writenow.app.state.UserState
import java.util.*

var defaultText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Arcu risus quis varius quam. Laoreet suspendisse interdum consectetur libero id faucibus nisl. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Quisque non tellus orci ac auctor augue."

fun capitalizeFirstLetter(string: String): String {
    return string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun defaultContentCreator(total: Int): MutableList<String> {
    val list = mutableListOf<String>()

    for (i in 0..total) {
        list.add(defaultText)
    }

    return list
}

/**
 * This is a workaround for the issue with [LaunchedEffect] not being called on the first composition.
 *
 * @param block The block to be executed. Anything passed to this block will be executed
 * only once when the composition is first mounted.
 */
@Composable
fun LaunchedEffectOnce(block: suspend CoroutineScope.() -> Unit) {
    val isMounted = remember { mutableStateOf(false) }

    LaunchedEffect(isMounted.value) {
        if (!isMounted.value) {
            block()
        }

        isMounted.value = true
    }
}

fun getPostedDate(postedDate: String): String {
    if (postedDate.isNotBlank()) {
        val splitDateTime = postedDate.split(" ")
        val date = splitDateTime[0].split("-")
        val year = date[0]
        val month = date[1]
        val day = date[2]

        return "$month/$day"
    }

    return ""
}

@OptIn(ExperimentalMaterialApi::class)
fun openPostMenu(currentPost: Post?, scope: CoroutineScope, state: ModalBottomSheetState) {
    UserState.isEllipsisClicked = true
    UserState.isCommentClicked = false
    UserState.isPostClicked = false
    UserState.selectedPost = currentPost

    if (UserState.isEllipsisClicked) scope.launch { state.show() }
}