package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.core.theme.AppIcons
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectingTopBar(
    isSelecting: MutableState<Boolean>,
    showSelectOptions: MutableState<Boolean>,
    selectedFiles: SnapshotStateList<FileModel>,
    selectedFolders: SnapshotStateList<FolderModel>,
    files: List<FileModel>,
    folders: List<FolderModel>,
) {

    val allCount = remember(selectedFiles.size, selectedFolders.size) {
        selectedFiles.size + selectedFolders.size
    }
    val allSelected = remember(allCount) {
        allCount > 0 && selectedFiles.size == files.size && selectedFolders.size == folders.size
    }

    TopAppBar(
        title = { Text(text = "$allCount Selected") },
        actions = {
            TextButton(onClick = {
                selectedFiles.clear()
                selectedFolders.clear()
                if (!allSelected) {
                    selectedFiles.addAll(files)
                    selectedFolders.addAll(folders)
                }
            }) {
                if (allSelected) Icon(
                    imageVector = AppIcons.checkbox,
                    contentDescription = ""
                ) else Icon(
                    imageVector = AppIcons.checkboxOutline,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "All")
            }
            TextButton(onClick = {
                selectedFiles.clear()
                selectedFolders.clear()
                isSelecting.value = false
            }) {
                Text(text = "Cancel")
            }
            IconButton(
                onClick = { showSelectOptions.value = true },
                enabled = allCount > 0
            ) {
                Icon(imageVector = AppIcons.more, contentDescription = "")
            }
        }
    )
}