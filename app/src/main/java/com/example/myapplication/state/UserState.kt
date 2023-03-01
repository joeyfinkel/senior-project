package com.example.myapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The global state of the user.
 */
object UserState {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
}
