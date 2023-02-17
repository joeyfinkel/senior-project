package com.example.myapplication.components.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.components.AnimatedColumn

/**
 * The basic layout for the registration.
 * It renders a centered title and the footer with one button.
 */
@Composable
fun RegistrationLayout(
    text: String,
    hasIcon: Boolean = false,
    onIconClick: (() -> Unit)? = null,
    content: @Composable() (() -> Unit)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .width(100.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (hasIcon) Arrangement.Start else Arrangement.Center
                ) {
                    if (hasIcon && onIconClick != null) {
                        IconButton(onClick = onIconClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Text(text, textAlign = TextAlign.Center, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
                content()
            }
        }
    }

}