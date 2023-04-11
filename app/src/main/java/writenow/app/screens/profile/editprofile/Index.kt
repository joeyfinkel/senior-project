package writenow.app.screens.profile.editprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import writenow.app.components.ClickableRow
import writenow.app.components.profile.*
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfile(navController: NavController) {
    var bio by remember { mutableStateOf("") }
    var uploadedImage by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val darkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val inputStream = context.contentResolver.openInputStream(it)
                BitmapFactory.decodeStream(inputStream)
            }
            val width = (bitmap!!.width * 0.5).toInt()
            val height = (bitmap!!.height * 0.5).toInt()

            UserState.bitmap = Bitmap.createScaledBitmap(bitmap!!, width, height, true)
            uploadedImage = true
        }
    }
    // End of stuff for profile picture


    LaunchedEffect(uploadedImage) {
        // Saves the image to the internal storage
        if (uploadedImage) {
            val directory = context.filesDir
            val file = File(directory, "${UserState.id}_profile_picture.png")
            val stream = withContext(Dispatchers.IO) { FileOutputStream(file) }

            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)

            withContext(Dispatchers.IO) {
                stream.flush()
                stream.close()
            }
        }
    }

    ProfileLayout(title = "Edit profile",
        topText = "Change photo",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) },
        accountIconAction = { launcher.launch("image/*") },
        content = { _, _ ->
            item { Spacer(modifier = Modifier.height(25.dp)) }
            item {
                Section(title = "About you") {
                    ClickableRow(
                        key = "Name",
                        value = UserState.displayName,
                    ) { navController.navigate(Screens.EditName) }
                    ClickableRow(
                        key = "Username",
                        value = UserState.username
                    ) { navController.navigate(Screens.EditUsername) }
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
                                if (UserState.bio.isEmpty() || UserState.bio.isBlank()) Text(text = "Add some more info about yourself")
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
                            trailingIcon = { Text(text = "${UserState.bio.length}/500") })
                    }
                }
            }
        })

}


