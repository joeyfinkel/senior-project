package writenow.app.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrange

@Composable
fun Section(
    title: String? = null,
    columnSpacing: Dp = 15.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (title != null) Text(text = title, color = PersianOrange, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(columnSpacing)) {
            content()
        }
    }
}