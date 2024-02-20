package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.presentation.components.GridTileComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.theme.AppIcons
import android.ifeanyi.read.core.util.dwdm
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    id: UUID,
    name: String,
    libraryVM: LibraryViewModel = hiltViewModel(),
) {
    val state = libraryVM.state.collectAsState().value

    val context = LocalContext.current
    val config = LocalConfiguration.current
    val locale = Locale.getDefault()

    val isSelecting = remember { mutableStateOf(false) }
    val selectedFiles = remember { mutableStateListOf<FileModel>() }
    val selectedFolders = remember { mutableStateListOf<FolderModel>() }

    val renameItem = remember { mutableStateOf(false) }
    val showSelectOptions = remember { mutableStateOf(false) }
    val moveFiles = remember { mutableStateOf(false) }


    fun onFileClick(file: FileModel) {
        if (!isSelecting.value) {
            SpeechService.updateModel(context, file)
            return
        }
        if (selectedFiles.contains(file)) {
            selectedFiles.remove(file)
        } else {
            selectedFiles.add(file)
        }
    }

    LaunchedEffect(key1 = Unit) {
        libraryVM.getFolderFiles(id)
    }

    Scaffold(
        topBar = {
            if (isSelecting.value) {
                SelectingTopBar(
                    isSelecting,
                    showSelectOptions,
                    selectedFiles,
                    selectedFolders,
                    state.folderFiles,
                    emptyList()
                )
            } else {
                TopAppBar(title = { Text(text = name) })
            }
        }
    ) { padding ->
        SelectingDialog(
            padding,
            isSelecting,
            moveFiles,
            renameItem,
            showSelectOptions,
            selectedFiles,
            selectedFolders,
            libraryVM
        )
        if (renameItem.value) {
            RenameSheet(
                renameItem,
                selectedFiles.firstOrNull(),
                null,
                libraryVM
            ) {
                renameItem.value = false
                isSelecting.value = false
                selectedFiles.clear()
            }
        }
        if (moveFiles.value) {
            MoveFilesSheet(
                moveFiles,
                selectedFiles,
                libraryVM
            ) {
                moveFiles.value = false
                isSelecting.value = false
                selectedFiles.clear()
            }
        }
        Column(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 200.dp)
            ) {
                items(state.folderFiles) { file ->
                    Box {
                        GridTileComponent(
                            asset = {
                                Icon(
                                    imageVector = file.icon(),
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(30.dp),
                                )
                            },
                            title = file.name,
                            subtitle = "${file.type.name.lowercase()} • ${file.progress}%\n${
                                file.date.dwdm(
                                    locale
                                )
                            }",
                            modifier = Modifier
                                .width(((config.screenWidthDp - 50) / 3).dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            isSelecting.value =
                                                !isSelecting.value
                                        }
                                    )
                                },
                            onClick = { onFileClick(file) },
                            onLongPress = {
                                if (isSelecting.value) return@GridTileComponent
                                isSelecting.value = true
                                selectedFiles.add(file)
                            }
                        )
                        if (isSelecting.value && selectedFiles.contains(file)) {
                            Icon(
                                imageVector = AppIcons.checkbox,
                                contentDescription = "",
                                modifier = Modifier.align(
                                    Alignment.TopEnd
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}