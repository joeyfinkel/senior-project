package com.example.myapplication.components.registration

//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.DefaultButton

@Composable
fun RegistrationFooter(
    btnText: String,
    additionalText: String = "",
    onBtnClick: () -> Unit,
    onTextClick: (() -> Unit)? = null
) {
    DefaultButton(btnText = btnText, onBtnClick = onBtnClick, width = 280.dp)
    if (additionalText.isNotEmpty() && onTextClick != null) {
        Text(
            text = additionalText,
            modifier = Modifier
                .clickable(onClick = onTextClick)
        )
    }
}