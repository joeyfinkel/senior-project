package com.example.myapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object FollowersOrFollowingState {
    var selected by mutableStateOf("Followers")
}