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
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
    var isBottomOverlay by mutableStateOf(false)

    operator fun component1() = firstName
    operator fun component2() = lastName
    operator fun component3() = email
    operator fun component4() = password
    operator fun component5() = userId
    operator fun component6() = isValid
    operator fun component7() = isCommentClicked
    operator fun component8() = isEllipsisClicked
}
