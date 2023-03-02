package com.example.myapplication.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.*
import com.example.myapplication.components.post.Post
import com.example.myapplication.components.post.PostActions
import com.example.myapplication.screens.*
import com.example.myapplication.screens.profile.FollowersOrFollowing
import com.example.myapplication.screens.profile.Profile
import com.example.myapplication.screens.registration.Information
import com.example.myapplication.screens.registration.Names
import com.example.myapplication.screens.registration.Username
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.MyApplicationTheme

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

/**
 * This function handles the app's routing. To add a new screen to the app, call [composable] with
 * the route of the new [Screens] and provide it with that screen's component.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()

    NavHost(navController = navController, startDestination = Screens.MainScreen.route) {
        //region Main Screen
        composable(Screens.MainScreen.route) { MainScreen(navController) }
        //endregion
        //region Registration screens
        composable(Screens.NameRegistration.route) { Names(navController) }
        composable(Screens.InformationRegistration.route) { Information(navController) }
        composable(Screens.UsernameRegistration.route) { Username(navController) }
        //endregion
        //region Login
        composable(Screens.Login.route) { Login(navController) }
        //endregion
        //region Posts
        composable(Screens.Posts.route) {
            Layout(
                title = "WriteNow",
                navController = navController,
                lazyListState = lazyListState
            ) { state, scope ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(25.dp),
                    state = lazyListState
                ) {
                    items(20) {
                        Post(
                            userId = it,
                            username = "User ${it + 1}",
                            navController = navController,
                            actionRow = {
                                PostActions(
                                    Modifier.align(Alignment.CenterHorizontally),
                                    state,
                                    scope
                                )
                            }
                        )
                    }
                }
            }
        }
        //endregion
        //region User Profile
        composable(Screens.UserProfile.route) { Profile(navController) }
        //endregion
        //region Followers/Following List
        composable(Screens.FollowersOrFollowingList.route) { FollowersOrFollowing(navController) }
        //endregion
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme { Main() }
}