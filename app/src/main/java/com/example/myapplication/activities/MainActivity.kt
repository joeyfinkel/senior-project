package com.example.myapplication.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.Layout
import com.example.myapplication.components.Login
import com.example.myapplication.components.MainScreen
import com.example.myapplication.components.registration.Information
import com.example.myapplication.components.registration.Names
import com.example.myapplication.components.registration.Username
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    Main()
                }
            }
        }
    }
}

/**
 * This function handles the app's routing. To add a new screen to the app, call [composable] with
 * the route of the new [Screen] and provide it with that screen's component.
 */
@Composable
fun Main() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) { MainScreen(navController) }

        //region Registration screens
        composable(Screen.NameRegistration.route) { Names(navController) }
        composable(Screen.InformationRegistration.route) { Information(navController) }
        composable(Screen.UsernameRegistration.route) { Username(navController) }
        //endregion

        //region Login
        composable(Screen.Login.route) { Login(navController) }
        //endregion

        //region Posts
        composable(Screen.Posts.route) { Layout(title = "WriteNow") }
        //endregion
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme { Main() }
}