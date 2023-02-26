package com.example.myapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


object PostState {
    var isLiked by mutableStateOf(false)
    var comments by mutableStateOf(mutableListOf<CommentState>())
}