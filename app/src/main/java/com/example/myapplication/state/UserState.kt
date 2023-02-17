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
    var userId by mutableStateOf("")
    var isValid by mutableStateOf(false)
}

data class UserState2(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val isValid: Boolean
)