package com.example.myapplication.screens

/**
 * This class is used for adding new screens to the app.
 * @param route The screen's route.
 */
enum class Screens(val route: String, val alias: String? = null) {
    MainScreen("main"),
    NameRegistration("nameRegistration"),
    InformationRegistration("informationRegistration"),
    UsernameRegistration("usernameRegistration"),
    Login("login"),
    Posts("posts", "home"),
    UserProfile("userProfile"),
    FollowersOrFollowingList("followersOrFollowingList"),
    Search("search"),
    Notifications("notifications"),
    NewPost("newPost"),
    EditProfile("editProfile");
}
