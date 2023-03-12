package writenow.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SelectedUserState {
   var userId by mutableStateOf("")
   var username by mutableStateOf("")
    var displayName by mutableStateOf("")
}