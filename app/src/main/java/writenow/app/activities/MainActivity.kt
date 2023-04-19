package writenow.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import kotlinx.coroutines.launch
import writenow.app.components.*
import writenow.app.dbtables.Posts
import writenow.app.screens.*
import writenow.app.screens.posts.AllPosts
import writenow.app.screens.posts.EditPost
import writenow.app.screens.posts.NewPost
import writenow.app.screens.profile.FollowersOrFollowing
import writenow.app.screens.profile.Profile
import writenow.app.screens.profile.editprofile.EditBirthday
import writenow.app.screens.profile.editprofile.EditName
import writenow.app.screens.profile.editprofile.EditProfile
import writenow.app.screens.profile.editprofile.EditUsername
import writenow.app.screens.profile.settings.Notifications
import writenow.app.screens.profile.settings.Settings
import writenow.app.screens.profile.settings.account.AccountSettings
import writenow.app.screens.profile.settings.account.Privacy
import writenow.app.screens.profile.settings.posts.DeletedPosts
import writenow.app.screens.registration.Information
import writenow.app.screens.registration.Names
import writenow.app.screens.registration.Username
import writenow.app.state.GlobalState
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.ui.theme.WriteNowTheme
import writenow.app.utils.createNotificationChannel
import writenow.app.utils.getProfilePicture
import writenow.app.utils.sendNotification

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val context = this@MainActivity

            GlobalState.provide(context)

            if (GlobalState.user != null) {
                val user = GlobalState.user!!

                UserState.isLoggedIn = true
                UserState.id = user.uuid
                UserState.firstName = user.firstName
                UserState.username = user.username
                UserState.bio = user.bio.toString()
                UserState.bitmap = getProfilePicture(context, user.uuid)
                UserState.selectedDays =
                    if (user.activeDays != null) user.activeDays.split(',').toSet() else setOf()
                UserState.activeHours.start =
                    if (user.activeHoursStart != null) user.activeHoursStart.toString() else ""
                UserState.activeHours.end =
                    if (user.activeHoursEnd != null) user.activeHoursEnd.toString() else ""
            }

            UserState.getHasPosted()
            createNotificationChannel(context)
            sendNotification(context, UserState.activeHours, UserState.selectedDays)

            if (PostState.allPosts.isEmpty()) {
                PostState.isLoading = true
                PostState.allPosts = Posts.getToDisplay()
                PostState.isLoading = false
            }

            if (UserState.posts.isEmpty()) {
                UserState.posts = Posts.getByUser(UserState.id).toMutableList()
            }

            if (UserState.likedPosts.isEmpty()) {
                UserState.likedPosts = Posts.getLikedPosts(UserState.id).toMutableList()
            }

            if (PostState.deletedPosts.isEmpty()) {
                PostState.deletedPosts = Posts.getDeleted(UserState.id).toMutableList()
            }
        }

        setContent {
            WriteNowTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState(0)
    val localContext = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (UserState.isLoggedIn) Screens.Posts else Screens.MainScreen
    ) {
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
        //region Posts - Home & New Post & Edit Post
        composable(Screens.Posts) { AllPosts(navController, lazyListState) }
        composable(Screens.NewPost) { NewPost(navController) }
        composable(Screens.EditPost) { EditPost(navController) }
        //endregion
        //region User Profile
        composable(Screens.UserProfile) { Profile(navController) }
        //region Edit Profile
        composable(Screens.EditProfile) { EditProfile(navController) }
        composable(Screens.EditName) { EditName(navController) }
        composable(Screens.EditUsername) { EditUsername(navController) }
        composable(Screens.EditBirthday) { EditBirthday(navController) }
        //endregion
        //region Followers/Following List
        composable(Screens.FollowersOrFollowingList) { FollowersOrFollowing(navController) }
        //endregion
        //region Settings
        composable(Screens.Settings) { Settings(navController) }
        composable(Screens.AccountSettings) { AccountSettings(navController) }
        composable(Screens.Privacy) { Privacy(navController) }
        composable(Screens.DeletedPosts) { DeletedPosts(navController, lazyListState) }
        composable(Screens.NotificationsSettings) { Notifications(navController) }
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
                Text(text = "Notifications will be here")
            }
        }
        //endregion
    }

    BackHandler {
        returnToMainScreen(navController)
        closeApp(navController, localContext)
    }

}