package writenow.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.AnimatedColumn
import writenow.app.components.DefaultButton

@Composable
fun MainScreen(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .width(100.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.logo_text),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Column {
                DefaultButton(width = 280.dp, btnText = "Login") {
                    navController.navigate(Screens.Login)
                }
                DefaultButton(width = 280.dp, btnText = "Register") {
                    navController.navigate(Screens.NameRegistration)
                }
            }
        }
    }
}