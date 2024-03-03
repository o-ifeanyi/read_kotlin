package android.ifeanyi.read.app.presentation.views.library


import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.presentation.components.GridTileComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.core.theme.AppIcons
import android.ifeanyi.read.core.util.dwdm
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MoveFilesSheet(
    moveFiles: MutableState<Boolean>,
    selectedFiles: SnapshotStateList<FileModel>,
    libraryVM: LibraryViewModel,
    onDone: () -> Unit,
) {
    val state = libraryVM.state.collectAsState().value

    val locale = Locale.getDefault()
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    fun onSelect(folder: FolderModel) {
        coroutineScope.launch {
            libraryVM.moveToFolder(id = folder.id, files = selectedFiles)
            modalSheetState.hide()
        }.invokeOnCompletion {
            onDone.invoke()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { moveFiles.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Select Folder", style = MaterialTheme.typography.titleLarge)

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 200.dp)
            ) {
                items(state.folders) { folder ->
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
                            subtitle = folder.date.dwdm(locale),
                            tonalElevation = 2.dp,
                            onClick = { onSelect(folder) }
                        )
                    }
                }
            }
        }
    }
}