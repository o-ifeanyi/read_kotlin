package android.ifeanyi.read.core.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SnackBarState(val hasMessage: Boolean = false, val message: String = "")
object SnackBarService: ViewModel() {
    private val _state = MutableStateFlow(SnackBarState())
    val state = _state.asStateFlow()

    fun displayMessage(message: String) = viewModelScope.launch {
        _state.update { it.copy(hasMessage = true, message = message) }
        delay(4000L)
        _state.update { it.copy(hasMessage = false, message = "") }
    }
}