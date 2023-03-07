package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Primary

@Composable
fun DropdownList(
    modifier: Modifier = Modifier,
    options: List<String>,
    onOptionClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column {
        Box(
            modifier = Modifier
                .then(modifier)
                .background(color = Primary, shape = RoundedCornerShape(16.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ClickableText(
                    text = AnnotatedString(options[selectedIndex]),
                    modifier = Modifier.padding(8.dp),
                    onClick = { expanded = true }
                )
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.expand_less else R.drawable.expand_more),
                    contentDescription = if (expanded) "Close" else "Open",
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        selectedIndex = index
                        onOptionClick(option)
                        expanded = false
                    }
                )
            }
        }
    }
}