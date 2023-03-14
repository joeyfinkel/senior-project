package writenow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrangeLight

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    width: Dp = 110.dp,
    spacedBy: Dp = 15.dp,
    borderRadius: Dp = 20.dp,
    btnText: String,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .widthIn(min = width)
            .then(modifier),
        onClick = onBtnClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = PersianOrangeLight,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(borderRadius),
        enabled = enabled
    ) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacedBy),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = icon, contentDescription = "")
                Text(btnText)
            }
        } else {
            Text(btnText)
        }
    }
}