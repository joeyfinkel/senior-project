package com.example.myapplication.screens

/**
 * This class is used for adding new screens to the app.
 * @param route The screen's route.
 */
enum class Screens(val route: String) {
    MainScreen("main"),
    NameRegistration("nameRegistration"),
    InformationRegistration("informationRegistration"),
    UsernameRegistration("usernameRegistration"),
    Login("login"),
    Posts("posts"),
    UserProfile("userProfile"),
    FollowersOrFollowingList("followersOrFollowingList"),
    EditProfile("editProfile")
}
