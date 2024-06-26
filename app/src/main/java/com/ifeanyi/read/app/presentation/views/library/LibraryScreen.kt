package com.ifeanyi.read.app.presentation.views.library

import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.FolderModel
import com.ifeanyi.read.app.presentation.components.GridTileComponent
import com.ifeanyi.read.app.presentation.components.TextFieldComponent
import com.ifeanyi.read.app.presentation.components.ListTileComponent
import com.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import com.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import com.ifeanyi.read.core.enums.DisplayStyle
import com.ifeanyi.read.core.route.Routes
import com.ifeanyi.read.core.services.SpeechService
import com.ifeanyi.read.core.theme.AppIcons
import com.ifeanyi.read.core.util.dwdm
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    controller: NavHostController,
    libraryVM: LibraryViewModel = hiltViewModel(),
    settingsVM: SettingsViewModel = hiltViewModel()
) {
    val state = libraryVM.state.collectAsState().value
    val settingsState = settingsVM.state.collectAsState().value

    val config = LocalConfiguration.current
    val locale = Locale.getDefault()

    val searchText = remember { mutableStateOf("") }

    val isSelecting = remember { mutableStateOf(false) }
    val selectedFiles = remember { mutableStateListOf<FileModel>() }
    val selectedFolders = remember { mutableStateListOf<FolderModel>() }


    val showMore = remember { mutableStateOf(false) }
    val showSort = remember { mutableStateOf(false) }
    val createFolder = remember { mutableStateOf(false) }
    val renameItem = remember { mutableStateOf(false) }
    val showSelectOptions = remember { mutableStateOf(false) }
    val moveFiles = remember { mutableStateOf(false) }

    fun onFileClick(file: FileModel) {
        if (!isSelecting.value) {
            SpeechService.updateModel(file)
            return
        }
        if (selectedFiles.any { it.id == file.id }) {
            selectedFiles.removeIf { it.id == file.id }
        } else {
            selectedFiles.add(file)
        }
    }

    fun onFolderClick(folder: FolderModel) {
        if (!isSelecting.value) {
            val folderRoute = "${Routes.FolderScreen.name}/${folder.id}/${folder.name}"
            controller.navigate(folderRoute)
            return
        }
        if (selectedFolders.contains(folder)) {
            selectedFolders.remove(folder)
        } else {
            selectedFolders.add(folder)
        }
    }

    Scaffold(
        topBar = {
            if (isSelecting.value) {
                SelectingTopBar(
                    isSelecting,
                    showSelectOptions,
                    selectedFiles,
                    selectedFolders,
                    state.files,
                    state.folders
                )
            } else {
                TopAppBar(
                    title = { Text(text = "Library") },
                    actions = {
                        IconButton(onClick = { showMore.value = !showMore.value }) {
                            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "")
                        }
                    }
                )
            }
        }
    ) { padding ->
        DefaultDialog(padding, createFolder, showMore, showSort, isSelecting, settingsVM)
        SortDialog(padding, showSort, showMore, libraryVM)
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
        if (createFolder.value) {
            CreateFolderSheet(createFolder, libraryVM)
        }
        if (renameItem.value) {
            RenameSheet(
                renameItem,
                selectedFiles.firstOrNull(),
                selectedFolders.firstOrNull(),
                libraryVM
            ) {
                renameItem.value = false
                isSelecting.value = false
                selectedFiles.clear()
                selectedFolders.clear()
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
                selectedFolders.clear()
            }
        }
        Column(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextFieldComponent(
                value = searchText,
                label = { Text(text = "Search") },
                onValueChange = {
                    if (it.isEmpty()) return@TextFieldComponent
                    libraryVM.search(it)
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )

            val files = if (searchText.value.isEmpty()) state.files else state.searchedFiles
            val folders = if (searchText.value.isEmpty()) state.folders else state.searchedFolders
            val counts = mutableMapOf<UUID, MutableIntState>()

            if (folders.isNotEmpty() && counts.isEmpty()) {
                for (folder in folders) {
                    counts[folder.id] = libraryVM.getFolderFilesCount(folder.id)
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 200.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                if (folders.isEmpty() && files.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .height((config.screenHeightDp * 0.7).dp)
                                .fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = AppIcons.folder,
                                    contentDescription = "Folder",
                                    modifier = Modifier.size(60.dp)
                                )
                                Text(
                                    text = "Nothing to see yet",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Items you previously listened to will show up here",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                if (folders.isNotEmpty()) {
                    when (settingsState.displayStyle) {
                        DisplayStyle.Grid -> {
                            item {
                                Text(text = "Folders", style = MaterialTheme.typography.titleSmall)
                                Spacer(modifier = Modifier.height(10.dp))
                                FlowRow(
                                    maxItemsInEachRow = 3,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    folders.map { folder ->
                                        Box {
                                            GridTileComponent(
                                                asset = {
                                                    Icon(
                                                        imageVector = AppIcons.folder,
                                                        contentDescription = "Icon",
                                                        modifier = Modifier.size(30.dp),
                                                    )
                                                },
                                                title = folder.name,
                                                subtitle = "${counts[folder.id]?.intValue ?: 0} items\n${
                                                    folder.date.dwdm(
                                                        locale
                                                    )
                                                }",
                                                modifier = Modifier
                                                    .width(((config.screenWidthDp - 61) / 3).dp),

                                                onClick = { onFolderClick(folder) },
                                                onLongPress = {
                                                    if (isSelecting.value) return@GridTileComponent
                                                    isSelecting.value = true
                                                    selectedFolders.add(folder)
                                                }
                                            )
                                            if (isSelecting.value && selectedFolders.contains(folder)) {
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

                        DisplayStyle.List -> {
                            item {
                                Text(text = "Folders", style = MaterialTheme.typography.titleSmall)
                            }
                            items(folders) { folder ->
                                Box {
                                    ListTileComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterStart),
                                        asset = {
                                            Icon(
                                                imageVector = AppIcons.folder,
                                                contentDescription = "Icon",
                                                modifier = Modifier.size(40.dp),
                                            )
                                        },
                                        title = folder.name,
                                        subtitle = "${counts[folder.id]?.intValue ?: 0} items • ${
                                            folder.date.dwdm(
                                                locale
                                            )
                                        }",
                                        onClick = { onFolderClick(folder) },
                                        onLongPress = {
                                            if (isSelecting.value) return@ListTileComponent
                                            isSelecting.value = true
                                            selectedFolders.add(folder)
                                        }
                                    )
                                    if (isSelecting.value && selectedFolders.contains(folder)) {
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
                if (files.isNotEmpty()) {
                    when (settingsState.displayStyle) {
                        DisplayStyle.Grid -> {
                            item {

                                Text(text = "Files", style = MaterialTheme.typography.titleSmall)
                                Spacer(modifier = Modifier.height(10.dp))

                                FlowRow(
                                    maxItemsInEachRow = 3,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    files.map { file ->
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
                                                subtitle = "${file.type.name.lowercase()} • ${file.absProgress}%\n${
                                                    file.date.dwdm(
                                                        locale
                                                    )
                                                }",
                                                modifier = Modifier
                                                    .width(((config.screenWidthDp - 61) / 3).dp)
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
                                            if (isSelecting.value && selectedFiles.any { it.id == file.id }) {
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

                        DisplayStyle.List -> {
                            item {
                                Text(text = "Files", style = MaterialTheme.typography.titleSmall)
                            }
                            items(files) { file ->
                                Box {
                                    ListTileComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterStart),
                                        asset = {
                                            Icon(
                                                imageVector = file.icon(),
                                                contentDescription = "Icon",
                                                modifier = Modifier.size(40.dp),
                                            )
                                        },
                                        title = file.name,
                                        subtitle = "${file.type.name.lowercase()} • ${file.absProgress}% • ${
                                            file.date.dwdm(
                                                locale
                                            )
                                        }",
                                        onClick = { onFileClick(file) },
                                        onLongPress = {
                                            if (isSelecting.value) return@ListTileComponent
                                            isSelecting.value = true
                                            selectedFiles.add(file)
                                        }
                                    )
                                    if (isSelecting.value && selectedFiles.any { it.id == file.id }) {
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
        }
    }
}

