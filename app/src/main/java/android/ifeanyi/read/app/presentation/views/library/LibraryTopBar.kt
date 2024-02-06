package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.presentation.viewmodel.LibraryState
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LibraryTopBar(
    isSelecting: MutableState<Boolean>,
    selected: SnapshotStateList<LibraryModel>,
    allSelected: Boolean,
    state: LibraryState,
    libraryViewModel: LibraryViewModel,
    showMore: MutableState<Boolean>
) {
    if (isSelecting.value) {
        TopAppBar(
            title = {
                Text(
                    text = "${selected.size} Selected",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            actions = {
                IconButton(onClick = {
                    if (allSelected) {
                        selected.clear()
                    } else {
                        selected.clear()
                        selected.addAll(state.items)
                    }
                }) {
                    Icon(
                        imageVector = if (allSelected) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    for (model in selected) {
                        libraryViewModel.deleteItem(model)
                    }
                    selected.clear()
                }, enabled = selected.isNotEmpty()) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = "")
                }
                TextButton(onClick = { isSelecting.value = !isSelecting.value }) {
                    Text(text = "Done")
                }
            }
        )

    } else {
        TopAppBar(
            title = { Text(text = "Library") },
            actions = {
                IconButton(onClick = { showMore.value = !showMore.value }) {
                    Icon(imageVector = Icons.Rounded.MoreHoriz, contentDescription = "")
                }
            }
        )
    }
}