package com.example.myapplication.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Primary

@Composable
fun <T> Tabs(
    tabs: List<T>,
    selectedTabIndex: Int,
    onClick: (index: Int) -> Unit,
    tabContent: @Composable () -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.LightGray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Primary
            )
        },
    ) {
        tabs.forEachIndexed { index, t ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClick(index) },
                modifier = Modifier.height(48.dp),
            ) {
                if (t is String) Text(text = t)
                if (t is ImageVector) Icon(
                    imageVector = t,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                if (t is Painter) Icon(
                    painter = t,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    tabContent()
}