package com.example.myapplication.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.Login
import com.example.myapplication.components.registration.Information
import com.example.myapplication.components.registration.Names
import com.example.myapplication.components.registration.Username
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.NameRegistration.route) {
        // Registration screens
        composable(Screen.NameRegistration.route) {
            Names(
                onButtonClick = { navController.navigate(Screen.InformationRegistration.route) },
                onTextClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.InformationRegistration.route) {
            Information(
                onButtonClick = { navController.navigate(Screen.InformationRegistration.route) },
                onBackButtonClick = { navController.navigate(Screen.NameRegistration.route) }
            )
        }
        composable(Screen.UsernameRegistration.route) {
            Username(
                onButtonClick = { println("Go to main view now...") },
                onBackButtonClick = { navController.navigate(Screen.InformationRegistration.route) }
            )
        }

        // Login screen
        composable(Screen.Login.route) {
            Login()
        }

        // Main view
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Main()
    }
}