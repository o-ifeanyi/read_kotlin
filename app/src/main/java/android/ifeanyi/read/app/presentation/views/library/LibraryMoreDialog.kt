package android.ifeanyi.read.app.presentation.views.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LibraryMoreDialog(
    padding: PaddingValues,
    showMore: MutableState<Boolean>,
    showSort: MutableState<Boolean>,
    isSelecting: MutableState<Boolean>,
    listStyle: MutableState<ListStyle>
) {
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
            expanded = showMore.value,
            onDismissRequest = { showMore.value = !showMore.value },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            DropdownMenuItem(
                text = { Text(text = "New Folder") },
                onClick = {
                    showMore.value = false
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.CreateNewFolder, contentDescription = "")
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Sort") },
                onClick = {
                    showMore.value = false
                    showSort.value = true
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Sort,
                        contentDescription = ""
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Select Multiple") },
                onClick = {
                    showMore.value = false
                    isSelecting.value = true
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.Checklist, contentDescription = "")
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(text = "List View") },
                onClick = {
                    showMore.value = false
                    listStyle.value = ListStyle.List
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ViewList,
                        contentDescription = ""
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Grid View") },
                onClick = {
                    showMore.value = false
                    listStyle.value = ListStyle.Grid
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.GridView, contentDescription = "")
                }
            )
        }
    }
}
