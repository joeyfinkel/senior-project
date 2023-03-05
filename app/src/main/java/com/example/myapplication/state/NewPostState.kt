package com.example.myapplication.state

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.focus.FocusRequester

object NewPostState {
    var focusRequester = FocusRequester()
}

fun FocusRequester.showKeyboard(context: Context) {
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = (context as Activity).window.decorView.findViewById<View>(android.R.id.content)

    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun FocusRequester.hideKeyboard(context: Context) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = (context as Activity).window.decorView.findViewById<View>(android.R.id.content)

    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}