package writenow.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import writenow.app.dbtables.Post
import writenow.app.state.UserState
import java.io.File
import java.util.*

var defaultText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Arcu risus quis varius quam. Laoreet suspendisse interdum consectetur libero id faucibus nisl. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Quisque non tellus orci ac auctor augue."

fun capitalizeFirstLetter(string: String): String {
    return string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
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

/**
 * This function gets the profile picture of the user from the internal storage.
 *
 * @param context The context of the application.
 * @param userId The id of the user.
 * @return The profile picture of the user. Will return null if the user does not have a profile picture.
 */
suspend fun getProfilePicture(context: Context, userId: Int): Bitmap? {
    val directory = context.filesDir
    val file = File(directory, "${userId}_profile_picture.png")
    val bitmap = mutableStateOf<Bitmap?>(null)

    if (file.exists()) {
        withContext(Dispatchers.IO) {
            val options = BitmapFactory.Options()

            BitmapFactory.decodeFile(file.absolutePath, options)

            var scaleFactory = 1

            if (options.outWidth > 1024) {
                scaleFactory = options.outWidth / 1024
            }

            options.inJustDecodeBounds = false
            options.inSampleSize = scaleFactory

            val stream = file.inputStream()
            bitmap.value = BitmapFactory.decodeStream(stream, null, options)

            stream.close()
        }
    }

    return bitmap.value
}

