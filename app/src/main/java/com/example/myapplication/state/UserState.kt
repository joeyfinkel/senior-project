package com.example.myapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplication.dbtables.Post

/**
 * The global state of the user.
 */
object UserState {
    var id by mutableStateOf(0)
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
    var likedPosts = mutableListOf<Post>()

    operator fun get(username: String): Any {
        return when (username) {
            "firstName" -> firstName
            "lastName" -> lastName
            "email" -> email
            "passwordHash" -> password
            "username" -> username
            else -> ""
        }
    }
}
