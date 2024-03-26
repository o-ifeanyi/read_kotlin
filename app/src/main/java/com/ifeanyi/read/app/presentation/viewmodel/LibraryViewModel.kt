package com.ifeanyi.read.app.presentation.viewmodel

import com.ifeanyi.read.app.data.LibraryRepository
import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.FolderModel
import com.ifeanyi.read.app.presentation.views.library.SortType
import android.net.Uri
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

data class LibraryState(
    val files: List<FileModel> = emptyList(),
    val searchedFiles: List<FileModel> = emptyList(),
    val folders: List<FolderModel> = emptyList(),
    val searchedFolders: List<FolderModel> = emptyList(),
    val folderFiles: List<FileModel> = emptyList(),
)

@HiltViewModel
class LibraryViewModel @Inject constructor(private val libraryRepository: LibraryRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    init {
        getAllFiles()
        getAllFolders()
    }

    private fun getAllFiles() = viewModelScope.launch {
        libraryRepository.getAllFiles().distinctUntilChanged().collect { items ->
            _state.update { it.copy(files = items) }
        }
    }

    fun insertItem(file: FileModel) = viewModelScope.launch {
        libraryRepository.insertItem(file)
    }

    fun updateItem(file: FileModel) = viewModelScope.launch {
        libraryRepository.updateItem(file)
    }

    fun deleteItem(file: FileModel) = viewModelScope.launch {
        val uri = Uri.parse(file.path)
        val localFile = File(uri.path ?: "")
        if (localFile.exists()) {
           localFile.delete()
        }
        libraryRepository.deleteItem(file)
    }

    private fun getAllFolders() = viewModelScope.launch {
        libraryRepository.getAllFolders().distinctUntilChanged().collect { items ->
            _state.update { it.copy(folders = items) }
        }
    }

    fun insertItem(folder: FolderModel) = viewModelScope.launch {
        libraryRepository.insertItem(folder)
    }

    fun updateItem(folder: FolderModel) = viewModelScope.launch {
        libraryRepository.updateItem(folder)
    }

    fun deleteItem(folder: FolderModel) = viewModelScope.launch {
        libraryRepository.deleteItem(folder)

        getFolderFiles(folder.id)
        delay(500)

        if (_state.value.folderFiles.isNotEmpty()) {
            for (file in _state.value.folderFiles) {
                deleteItem(file)
            }
        }
    }

    fun moveToFolder(id: UUID, files: List<FileModel>) = viewModelScope.launch {
        for (file in files) {
            libraryRepository.updateItem(file.copy(parent = id))
        }
    }

    fun getFolderFiles(id: UUID) = viewModelScope.launch {
        libraryRepository.getFolderFiles(id).distinctUntilChanged().collect { items ->
            _state.update { it.copy(folderFiles = items) }
        }
    }

    fun getFolderFilesCount(id: UUID): MutableIntState {
        val count = mutableIntStateOf(0)
        viewModelScope.launch {
            libraryRepository.getFolderFilesCount(id).distinctUntilChanged().collect { item ->
                count.intValue = item
            }
        }
        return count
    }

    fun search(query: String) = viewModelScope.launch {
        val value = _state.value
        _state.update {
            it.copy(
                searchedFiles = value.files.filter { file ->
                    file.name.lowercase().contains(query.lowercase())
                },
                searchedFolders = value.folders.filter { folder ->
                    folder.name.lowercase().contains(query.lowercase())
                }
            )
        }
    }

    fun sort(type: SortType) = viewModelScope.launch {
        val value = _state.value
        _state.update {
            it.copy(
                files = value.files.sortedBy { file ->
                    when (type) {
                        SortType.Date -> file.date
                        SortType.Name -> file.name
                    }.toString()
                },
                folders = value.folders.sortedBy { folder ->
                    when (type) {
                        SortType.Date -> folder.date
                        SortType.Name -> folder.name
                    }.toString()
                },
            )
        }
    }
}