package writenow.app.dbtables

data class Comment(
    val id: Int? = null,
    val userId: Int,
    val postId: Int,
    val text: String,
    val isDeleted: Int? = null,
    var dateCommented: String = "",
    var dateUpdated: String = "",
    val username: String = "",
    var isLiked: Boolean = false,
)

