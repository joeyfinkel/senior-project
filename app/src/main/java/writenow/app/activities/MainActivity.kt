package writenow.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import writenow.app.components.*
import writenow.app.dbtables.*
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
import writenow.app.utils.PushNotificationService
import writenow.app.utils.getProfilePicture
import java.time.LocalDate
import writenow.app.data.entity.Question as QuestionEntity
import writenow.app.data.entity.ReportReason as ReportReasonsEntity

class MainActivity : ComponentActivity() {
    private suspend fun getQuestion() {
        val question = GlobalState.question

        if (question != null) {
            UserState.currentQuestion = Question(
                id = question.id,
                text = question.text,
                author = question.author,
                category = question.category,
            )
        } else {
            UserState.currentQuestion = Questions.getRandom().let {
                Question(
                    id = it.id,
                    text = it.text,
                    author = it.author,
                    category = it.category,
                    dateRetrieved = it.dateRetrieved
                )
            }

            if (GlobalState.question == null) {
                val repo = GlobalState.questionRepository
                UserState.currentQuestion.let {
                    GlobalState.question = QuestionEntity(
                        id = it?.id ?: 0,
                        text = it?.text ?: "",
                        author = it?.author ?: 0,
                        category = it?.category ?: "",
                        dateRetrieved = it?.dateRetrieved ?: LocalDate.now().dayOfMonth
                    )
                }

                repo.addQuestion(GlobalState.question!!)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun onLoad(context: Context) {
        val user = GlobalState.user
        val followers = GlobalState.followers
        val followings = GlobalState.followings

        if (user != null) {
            val id = user.uuid
            val lastPost = Posts.getByUser(id).maxByOrNull { it.createdAt }

            Log.d("MainActivity", "onLoad: $lastPost")

            UserState.isLoggedIn = true
            UserState.hasPosted = lastPost != null
            UserState.isPostPrivate = user.isPostPrivate == 1
            UserState.id = id
            UserState.firstName = user.firstName
            UserState.username = user.username
            UserState.bio = user.bio.toString()
            UserState.bitmap = getProfilePicture(context, id)
            UserState.selectedDays =
                if (user.activeDays != null) user.activeDays.split(',').toSet() else setOf()
            UserState.activeHours.start =
                if (user.activeHoursStart != null) user.activeHoursStart.toString() else ""
            UserState.activeHours.end =
                if (user.activeHoursEnd != null) user.activeHoursEnd.toString() else ""
            UserState.isPostPrivate = user.isPostPrivate == 0

            UserState.following.addAll(Users.getFollowing(id))
            UserState.followers.addAll(Users.getFollowers(id))
        }

        PostState.fetchNewPosts(UserState.hasPosted, UserState.id).updateAll()
        Log.d("MainActivity", "onLoad: ${UserState.hasPosted}")
        // If there is a current question, set it as the current question, otherwise get a new question
        runBlocking { getQuestion() }

        if (followers?.isEmpty() == true) {
            UserState.followers =
                GlobalState.followerRepository.getFollowers()?.map { Follower(it.id) }
                    ?.toMutableList() ?: mutableListOf()
        }

        if (followings?.isEmpty() == true) {
            UserState.following = followings.map { Follower(it.id) }.toMutableList()
        }

        if (GlobalState.reportReasons?.isEmpty() == true) {
            GlobalState.reportReasonRepository.insertReportReasons(ReportReasons.getAll().map {
                ReportReasonsEntity(
                    it.id, it.reason
                )
            })

            GlobalState.reportReasons =
                GlobalState.reportReasonRepository.getReportReasons().toMutableList()
        }

        PushNotificationService().sendNotification(context)

        if (GlobalState.user?.uuid != null) UserState.updateActiveHours(GlobalState.user?.uuid!!)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val context = this@MainActivity
                val fetchedUser = GlobalState.user?.uuid?.let { Users.getCurrent(it) }

                GlobalState.provide(context)
                onLoad(context)
                fetchedUser?.let { user ->
                    GlobalState.user =
                        GlobalState.user?.copy(uuid = user.id.takeIf { GlobalState.user?.uuid != it }
                            ?: GlobalState.user?.uuid ?: 0,
                            firstName = user.firstName.takeIf { GlobalState.user?.firstName != it }
                                ?: GlobalState.user?.firstName ?: "",
                            lastName = user.lastName.takeIf { GlobalState.user?.lastName != it }
                                ?: GlobalState.user?.lastName ?: "",
                            email = user.email.takeIf { GlobalState.user?.email != it }
                                ?: GlobalState.user?.email ?: "",
                            password = user.passwordHash.takeIf { GlobalState.user?.password != it }
                                ?: GlobalState.user?.password ?: "",
                            username = user.username.takeIf { GlobalState.user?.username != it }
                                ?: GlobalState.user?.username ?: "",
                            displayName = user.displayName.takeIf { GlobalState.user?.displayName != it }
                                ?: GlobalState.user?.displayName,
                            bio = user.bio.takeIf { GlobalState.user?.bio != it }
                                ?: GlobalState.user?.bio,
                            activeDays = user.activeDays.takeIf { GlobalState.user?.activeDays != it }
                                ?: GlobalState.user?.activeDays,
                            activeHoursStart = user.preferences?.activeHours?.start.takeIf { GlobalState.user?.activeHoursStart != it }
                                ?: GlobalState.user?.activeHoursStart,
                            activeHoursEnd = user.preferences?.activeHours?.end.takeIf { GlobalState.user?.activeHoursEnd != it }
                                ?: GlobalState.user?.activeHoursEnd,
                            isPostPrivate = user.preferences?.isPrivate.takeIf { GlobalState.user?.isPostPrivate != it }
                                ?: GlobalState.user?.isPostPrivate)
                }
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
                Text(
                    text = "Search screen", color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        //endregion
        //region Notifications
        composable(Screens.Notifications) {
            Layout(navController = navController) { _, _ ->
                Text(
                    text = "Notifications will be here", color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        //endregion
    }

    BackHandler {
        returnToMainScreen(navController)
        closeApp(navController, localContext)
    }

}