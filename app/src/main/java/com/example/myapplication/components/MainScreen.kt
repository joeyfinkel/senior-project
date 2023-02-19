package com.example.myapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Screen

@Composable
fun MainScreen(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .width(100.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(com.example.myapplication.R.drawable.logo_text),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp)
                )
//                Spacer(modifier = Modifier.height(40.dp))
                DefaultButton(200.dp,"Login") { navController.navigate(Screen.Login.route) }
                DefaultButton(200.dp,"Register") { navController.navigate(Screen.NameRegistration.route) }
            }
        }
    }
}