package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.*
import com.example.myapplication.dbtables.Users
import com.example.myapplication.screens.*
import com.example.myapplication.screens.posts.AllPosts
import com.example.myapplication.screens.posts.NewPost
import com.example.myapplication.screens.profile.EditProfile
import com.example.myapplication.screens.profile.FollowersOrFollowing
import com.example.myapplication.screens.profile.Profile
import com.example.myapplication.screens.profile.Settings
import com.example.myapplication.screens.profile.editprofile.EditName
import com.example.myapplication.screens.profile.editprofile.EditUsername
import com.example.myapplication.screens.registration.Information
import com.example.myapplication.screens.registration.Names
import com.example.myapplication.screens.registration.Username
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Background
                ) {
                    Main()
                }
            }
        }
    }
}

fun returnToMainScreen(navController: NavController) {
    val currentDestination = navController.currentDestination?.route

    // If the user is on the login screen or the name registration screen, pressing the back button will take them to the main screen.
    if (currentDestination == Screens.NameRegistration || currentDestination == Screens.Login) {
        navController.navigate(Screens.MainScreen)
    }
}

fun closeApp(navController: NavController, localContext: Context) {
    val currentDestination = navController.currentDestination?.route

    // If the user is on the main screen or they are logged in, pressing the back button will close the app.
    if (currentDestination == Screens.MainScreen || UserState.isLoggedIn) {
        (localContext as? Activity)?.finish()
    }
}

/**
 * This function handles the app's routing. To add a new screen to the app, call [composable] with
 * the route of the new [Screens] and provide it with that screen's component.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterialApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()
    val localContext = LocalContext.current

//    GlobalScope.launch {
//        val emails = Users.getEmails()
//
//        for (email in emails) {
//            println(email)
//        }
//    }

    NavHost(navController = navController, startDestination = Screens.MainScreen) {
        //region Main Screen
        composable(Screens.MainScreen) { MainScreen(navController) }
        //endregion
        //region Registration - Name, Information, Username
        composable(Screens.NameRegistration) { Names(navController) }
        composable(Screens.InformationRegistration) { Information(navController) }
        composable(Screens.UsernameRegistration) { Username(navController) }
        //endregion
        //region Login
        composable(Screens.Login) { Login(navController) }
        //endregion
        //region Posts - Home & New Post
        composable(Screens.Posts) { AllPosts(navController, lazyListState) }
        composable(Screens.NewPost) { NewPost(navController) }
        //endregion
        //region User Profile
        composable(Screens.UserProfile) { Profile(navController) }
        //region Edit Profile
        composable(Screens.EditProfile) { EditProfile(navController) }
        composable(Screens.EditName) { EditName(navController) }
        composable(Screens.EditUsername) { EditUsername(navController) }
        //endregion
        //region Followers/Following List
        composable(Screens.FollowersOrFollowingList) { FollowersOrFollowing(navController) }
        //endregion
        //region Settings
        composable(Screens.Settings) { Settings(navController) }
        //endregion
        //endregion
        //region Search
        composable(Screens.Search) {
            Layout(navController = navController) { _, _ ->
                Text(text = "Search screen")

            }
        }
        //endregion
        //region Notifications
        composable(Screens.Notifications) {
            Layout(navController = navController) { _, _ ->
                Text(text = "Search screen")

            }
        }
        //endregion
    }

    BackHandler {
        returnToMainScreen(navController)
        closeApp(navController, localContext)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme { Main() }
}