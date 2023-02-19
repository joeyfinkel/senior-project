package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBar
import com.example.myapplication.ui.theme.DefaultRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(
    title: String
) {
    val items = listOf("Songs", "Artists", "Playlists")

    Scaffold(topBar = {
        Surface(
            color = AppBar,
            shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
        ) {
            SmallTopAppBar(
                title = {
                    Text(
                        title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = AppBar
                )
            )
        }
    }, content = { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                    items(20) { index -> Post() }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }, bottomBar = {
        Surface(
            color = AppBar,
            shape = RoundedCornerShape(topEnd = DefaultRadius, topStart = DefaultRadius)
        ) {
            BottomAppBar(containerColor = AppBar) {
                items.forEachIndexed { _, item ->
                    NavigationBarItem(icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = item
                        )
                    },
                        label = { Text(item) },
                        selected = false,
                        onClick = { println("Hello") })
                }
            }
        }
    })
}