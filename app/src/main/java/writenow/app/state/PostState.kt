package writenow.app.state

import writenow.app.dbtables.Posts

object PostState {
    var posts = Posts.create(3)
}