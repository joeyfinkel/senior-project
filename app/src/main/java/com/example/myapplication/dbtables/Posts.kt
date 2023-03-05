package com.example.myapplication.dbtables

import com.example.myapplication.utils.defaultText

data class Post(
    val id: Int,
    val userId: Int,
    val username: String,
    val text: String,
    val likes: Int,
    val comments: Int,
    val date: String,
    var isLiked: Boolean = false,
) {
    operator fun get(s: String): Any? {
        return when (s) {
            "id" -> id
            "userId" -> userId
            "text" -> text
            "likes" -> likes
            "comments" -> comments
            "date" -> date
            else -> null
        }
    }
}

class Posts private constructor() {
    companion object {
        /**
         * Creates a list of posts with default values for testing purposes
         * @param total The total number of posts to create
         */
        fun create(total: Int): MutableList<Post> {
            val posts = mutableListOf<Post>()

            for (i in 1..total) {
                posts.add(
                    Post(
                        id = i,
                        userId = i,
                        text = defaultText,
                        likes = 0,
                        comments = 0,
                        date = "2021-01-01",
                        username = "User $i"
                    )
                )
            }

            return posts
        }
    }
}