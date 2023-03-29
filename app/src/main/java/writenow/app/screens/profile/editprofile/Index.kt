package writenow.app.screens.profile.editprofile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.ClickableRow
import writenow.app.components.icons.AccountCircle
import writenow.app.components.profile.*
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor

// Gallery imports
import android.net.Uri
//
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import writenow.app.components.icons.AccountSquare

/*
TODO: Update UI to match the version from the figma
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfile(navController: NavController) {
    var bio by remember { mutableStateOf("") }
    val darkMode = isSystemInDarkTheme()

    // Stuff for profile picture
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        Log.d("uri", imageUri.toString())

        // Get bitmap
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                UserState.bitmap = ImageDecoder.decodeBitmap(source)
            }
            // Set pfp to bitmap
            Log.d("bitmap", bitmap.toString())
        }
    }
    // End of stuff for profile picture

    ProfileLayout(
        title = "Edit profile",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) }
    ) { innerPadding, _, _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    /*UserState.bitmap?.let {  btm ->
                        Image(bitmap = btm.asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier.size(75.dp))
                    }*/

                    // Account circle stuff
                    if (UserState.bitmap == null) {
                        // Account circle with default pfp
                        Log.d("Account Circle:", "default")
                        AccountCircle(
                            size = 75.dp,
                            onClick = {
                                // Open Gallery
                                launcher.launch("image/")
                            })
                    } else {
                        // Account circle with user's pfp
                        Log.d("Account Circle:", "pfp")
                        AccountSquare(
                            bitmap = UserState.bitmap,
                            size = 90.dp
                        ) {
                            // Open Gallery
                            launcher.launch("image/")
                        }
                    }

                    Text(text = "Change photo", color = MaterialTheme.colorScheme.onSurface)
                }
            }
            item { Spacer(modifier = Modifier.height(25.dp)) }
            item {
                Section(title = "About you") {
                    ClickableRow(
                        key = "Name",
                        value = UserState.displayName,
                        onClick = { navController.navigate(Screens.EditName) },
                    )
                    //ClickableRow(
                    //    key = "Username",
                   //     value = UserState.username,
                    //    onClick = { /*Going to leave this empty since allowing users to change this might cause problems*/ })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Bio", color = MaterialTheme.colorScheme.onSurface)
                        OutlinedTextField(
                            value = UserState.bio,
                            placeholder = {
                                if (UserState.bio.isEmpty() || UserState.bio.isBlank())
                                    Text(text = "Add some more info about yourself")
                            },
                            modifier = Modifier.fillMaxSize(),
                            onValueChange = { UserState.bio = it },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurface,
                                placeholderColor = placeholderColor(darkMode),
                                trailingIconColor = placeholderColor(darkMode),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = PersianOrange,
                                unfocusedBorderColor = PersianOrange,
                                cursorColor = PersianOrange
                            ),
                            trailingIcon = { Text(text = "${UserState.bio.length}/500") }
                        )
                    }
                }
            }
        }
    }
}


