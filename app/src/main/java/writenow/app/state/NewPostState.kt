package writenow.app.state

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

object NewPostState {
    var visibility by mutableStateOf("Public")
}

fun FocusRequester.showKeyboard(context: Context) {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = (context as Activity).window.decorView.findViewById<View>(android.R.id.content)

    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun FocusRequester.hideKeyboard(context: Context) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = (context as Activity).window.decorView.findViewById<View>(android.R.id.content)

    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}