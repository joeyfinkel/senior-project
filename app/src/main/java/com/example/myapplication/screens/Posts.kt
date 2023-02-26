package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.post.PostActions
import com.example.myapplication.components.post.PostContent
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.DefaultWidth
import com.example.myapplication.ui.theme.PostBG
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Posts(
    userId: Int,
    username: String,
    text: String,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .width(DefaultWidth + 100.dp)
            .height(DefaultWidth / 2)
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .border(2.dp, Primary, RoundedCornerShape(16.dp))
                .background(PostBG)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PostContent(
                    userId = userId,
                    username = username,
                    text = text,
                    navController = navController
                )
                PostActions(Modifier.align(Alignment.CenterHorizontally), state, coroutineScope)
            }
        }
    }
}
