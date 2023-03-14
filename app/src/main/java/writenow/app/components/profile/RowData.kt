package writenow.app.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import writenow.app.ui.theme.PersianOrange

@Composable
private fun PrimaryText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        modifier = Modifier.padding(8.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun RowData(primaryText: String, secondaryText: String, onClick: (() -> Unit)? = null) {
    val width = 60.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onClick == null) {
            Box(
                modifier = Modifier
                    .width(width)
                    .background(PersianOrange, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                PrimaryText(text = primaryText)
            }
        } else {
            TextButton(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.width(width),
                contentPadding = PaddingValues(0.dp)
            ) {
                PrimaryText(text = primaryText)
            }
        }
        Text(text = secondaryText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
    }
}
