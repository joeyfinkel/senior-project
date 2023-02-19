package com.example.myapplication

/**
 * This class is used for adding new screens to the app.
 * @param route The screen's route.
 * @param title The screen's title.
 */
enum class Screen(val route: String, val title: String? = "") {
    MainScreen("main"),
    NameRegistration("nameRegistration"),
    InformationRegistration("informationRegistration"),
    UsernameRegistration("usernameRegistration"),
    Login("login"),
    Posts("posts")
}
