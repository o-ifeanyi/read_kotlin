package com.ifeanyi.read.core.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SnackBarState(val hasMessage: Boolean = false, val message: String = "")
data class LoadingState(val isLoading: Boolean = false, val message: String = "")
object AppStateService: ViewModel() {
    private val _snackBar = MutableStateFlow(SnackBarState())
    private val _loader = MutableStateFlow(LoadingState())
    val snackBar = _snackBar.asStateFlow()
    val loader = _loader.asStateFlow()

    fun displayMessage(message: String) = viewModelScope.launch {
        _snackBar.update { it.copy(hasMessage = true, message = message) }
        delay(4000L)
        _snackBar.update { it.copy(hasMessage = false, message = "") }
    }

    fun displayLoader(message: String = "loading ...") = viewModelScope.launch {
        _loader.update { it.copy(isLoading = true, message = message) }
    }

    fun removeLoader() = viewModelScope.launch {
        _loader.update { it.copy(isLoading = false, message = "") }
    }
}