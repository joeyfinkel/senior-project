package com.example.myapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object CommentState {
    var userId by mutableStateOf("")
    var text by mutableStateOf("")
    var isLiked by mutableStateOf(false)
}