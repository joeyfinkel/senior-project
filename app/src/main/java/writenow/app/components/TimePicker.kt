package writenow.app.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//@Composable
//fun InfiniteScrollingList() {
//    var items by remember { mutableStateOf((1..12).toList()) }
//    val lazyListState = rememberLazyListState()
//
//    LaunchedEffect(lazyListState) {
//        snapshotFlow { lazyListState.firstVisibleItemIndex }.distinctUntilChanged().collect { index ->
//            if (index == 0) {
//                items = (12 downTo 1).plus(items.subList(items.size/2, items.size)).toMutableList()
//            } else if (index == items.size - lazyListState.layoutInfo.visibleItemsInfo.last().index - 1) {
//                items = items.subList(items.size/2, items.size).plus((1..12)).toMutableList()
//            }
//        }
//    }
//
//    LazyColumn(state = lazyListState) {
//        items(items.size) {
//            Text(text = "$it", fontSize = 24.sp, modifier = Modifier.fillMaxWidth().padding(16.dp))
//        }
//    }
//}
//
//@Preview
//@Composable
//fun InfiniteScrollingListPreview() {
//    InfiniteScrollingList()
//}
//
//@Composable
//fun TimePicker() {
//    var hours = remember { mutableListOf<Int>() }
//    val minutes = (0..59).map { it.toString().padStart(2, '0') }
//    val amPm = listOf("AM", "PM")
//    val hoursState = rememberLazyListState()
//    val minutesState = rememberLazyListState()
//    var startIndex by remember { mutableStateOf(0) }
//    var endIndex by remember { mutableStateOf(min(startIndex + 10, hours.size - 1)) }
//
//    for (i in 1..12) hours.add(i)
//
//    var firstVisibleIndex by remember { mutableStateOf(0) }
//
//    LaunchedEffect(hoursState) {
//        snapshotFlow { hoursState.firstVisibleItemIndex }.collect { index ->
//            if (index == 0) {
//                hours.addAll(0, (1..12).reversed().toMutableList())
//                hours.addAll(hours.size, (1..12).toMutableList())
//            }
//            if (index == hours.size - 1) {
//                hours.addAll(hours.size, (1..12).toMutableList())
//                hours.addAll(0, (1..12).reversed().toMutableList())
//            }
//        }
//    }
//
////    LaunchedEffect(hoursState) {
////        snapshotFlow { hoursState.firstVisibleItemIndex }.distinctUntilChanged().collect { index ->
////            if (index == hours.size / 2) hours = hours + hours
////        }
////    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.8f)
//                .height(200.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                LazyColumn(
//                    state = hoursState, verticalArrangement = Arrangement.Center
//                ) {
//                    itemsIndexed(hours) { index, hour ->
//                        Text(
//                            text = hour.toString(),
//                            color = MaterialTheme.colorScheme.onSurface,
//                            fontSize = 30.sp
//                        )
//                    }
//                }
//                LazyColumn(state = minutesState) {
//                    itemsIndexed(minutes) { _, minute ->
//                        Text(
//                            text = minute,
//                            color = MaterialTheme.colorScheme.onSurface,
//                            fontSize = 30.sp
//                        )
//                    }
//                }
//            }
//        }
//        Column(
//            modifier = Modifier.selectableGroup(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            amPm.forEach { option ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                    modifier = Modifier.selectable(
//                        selected = false, onClick = { /*TODO*/ }, role = Role.RadioButton
//                    )
//                ) {
//                    RadioButton(
//                        selected = false, onClick = null, colors = RadioButtonDefaults.colors(
//                            selectedColor = MaterialTheme.colorScheme.primary,
//                            unselectedColor = MaterialTheme.colorScheme.onSurface
//                        ), modifier = Modifier.size(15.dp)
//                    )
//                    Text(
//                        text = option, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun TimePickerPreview() {
//    TimePicker()
//}

@Composable
fun Timepicker(dialogState: MaterialDialogState, title: String) {
    var selectTime by remember { mutableStateOf(LocalTime.now()) }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm").format(selectTime)
        }
    }

    MaterialDialog(dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(text = "Chose time")
            negativeButton(text = "Cancel")
        }) {
        timepicker(
            initialTime = LocalTime.now(),
            title = title,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                activeTextColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.primary,
                selectorColor = MaterialTheme.colorScheme.primary,
                selectorTextColor = MaterialTheme.colorScheme.onSurface,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                inactiveTextColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) { time ->
            selectTime = time
        }
    }
}

@Preview
@Composable
fun TimepickerPreview() {
    val state = rememberMaterialDialogState()

    state.show()
    Timepicker(dialogState = state, title = "Timepicker")
}