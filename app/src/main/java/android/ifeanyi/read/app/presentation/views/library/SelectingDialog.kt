package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectingDialog(
    padding: PaddingValues,
    isSelecting: MutableState<Boolean>,
    moveFiles: MutableState<Boolean>,
    renameItem: MutableState<Boolean>,
    showSelectOptions: MutableState<Boolean>,
    selectedFiles: SnapshotStateList<FileModel>,
    selectedFolders: SnapshotStateList<FolderModel>,
    libraryViewModel: LibraryViewModel,
) {
    val allCount = remember(selectedFiles.size, selectedFolders.size) {
        selectedFiles.size + selectedFolders.size
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding(),
                start = 15.dp, end = 15.dp,
            )
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = showSelectOptions.value,
            onDismissRequest = {
                showSelectOptions.value = false
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            DropdownMenuItem(
                text = { Text(text = "Delete") },
                onClick = {
                    for (file in selectedFiles) {
                        libraryViewModel.deleteItem(file)
                    }
                    for (folder in selectedFolders) {
                        libraryViewModel.deleteItem(folder)
                    }
                    selectedFiles.clear()
                    selectedFolders.clear()
                    showSelectOptions.value = false
                    isSelecting.value = false
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = ""
                    )
                },
            )

            if (selectedFiles.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text(text = "Move to folder") },
                    onClick = {
                        showSelectOptions.value = false
                        moveFiles.value = true
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.DriveFileMove,
                            contentDescription = ""
                        )
                    },
                )
            }

            if (allCount == 1) {
                DropdownMenuItem(
                    text = { Text(text = "Rename") },
                    onClick = {
                        showSelectOptions.value = false
                        renameItem.value = true
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DriveFileRenameOutline,
                            contentDescription = ""
                        )
                    },
                )
            }
        }
    }
}