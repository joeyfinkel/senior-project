package com.example.myapplication

import androidx.compose.runtime.Composable
import com.example.myapplication.components.registration.Names
import com.example.myapplication.components.registration.Information

enum class Screen(val route: String, val title: String? = "") {
    NameRegistration("nameRegistration"),
    InformationRegistration("informationRegistration"),
    UsernameRegistration("usernameRegistration"),
    Login("login")
}
