package com.example.myapplication.state

import com.example.myapplication.dbtables.Posts


object PostState {
    var posts = Posts.create(3)
}