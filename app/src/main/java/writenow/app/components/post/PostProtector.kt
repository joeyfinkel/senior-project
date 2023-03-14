package writenow.app.components.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import writenow.app.components.DefaultButton
import writenow.app.screens.Screens
import writenow.app.state.UserState

@Composable
fun PostProtector(navController: NavController) {
    val genericMessage = "If you'd like to be able to see the feed, please post something."
    val name = UserState.username.ifEmpty {
        if (UserState.firstName.isEmpty() && UserState.lastName.isEmpty()) {
            ""
        } else {
            "${UserState.firstName} ${UserState.lastName}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oops, you didn't post yet!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = if (name == "") genericMessage else "Hello $name, $genericMessage".trim(),
            textAlign = TextAlign.Justify,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(200.dp))
        DefaultButton(btnText = "Post") { navController.navigate(Screens.NewPost) }
    }
}
