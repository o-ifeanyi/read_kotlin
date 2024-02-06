package android.ifeanyi.read.app.presentation.viewmodel

import android.ifeanyi.read.app.data.LibraryRepository
import android.ifeanyi.read.app.data.models.LibraryModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryState(
    val items: List<LibraryModel> = emptyList()
)
@HiltViewModel
class LibraryViewModel @Inject constructor(private val libraryRepository: LibraryRepository): ViewModel() {
    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    init {
        getAllItems()
    }

    private fun getAllItems() = viewModelScope.launch {
        libraryRepository.getAllItems().distinctUntilChanged().collect { items ->
            _state.update { it.copy(items = items) }
        }
    }

    fun insertItem(libraryModel: LibraryModel) = viewModelScope.launch {
        libraryRepository.insertItem(libraryModel)
    }

    fun deleteItem(libraryModel: LibraryModel) = viewModelScope.launch {
        libraryRepository.deleteItem(libraryModel)
    }
}