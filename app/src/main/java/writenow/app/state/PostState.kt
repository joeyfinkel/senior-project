package writenow.app.state

import writenow.app.dbtables.Post

object PostState {
    var posts = mutableListOf<Post>()
}