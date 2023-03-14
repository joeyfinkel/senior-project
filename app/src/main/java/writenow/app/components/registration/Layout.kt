package writenow.app.components.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import writenow.app.components.AnimatedColumn

/**
 * The basic layout for the registration.
 * It renders a centered title and the footer with one button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationLayout(
    text: String,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    hasIcon: Boolean = false,
    onIconClick: (() -> Unit)? = null,
    content: @Composable() (() -> Unit)
) {
    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .width(100.dp)
                .padding(it)
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

                        Text(text = text, textAlign = TextAlign.Center, style = style)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    content()
                }
            }
        }
    }
}