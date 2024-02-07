package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class SortType { Date, Name }

@Composable
fun SortDialog(
    padding: PaddingValues,
    showSort: MutableState<Boolean>,
    showMore: MutableState<Boolean>,
    libraryViewModel: LibraryViewModel,
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
            expanded = showSort.value,
            onDismissRequest = {
                showSort.value = false
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            DropdownMenuItem(
                text = { Text(text = "Sort") },
                onClick = {
                    showSort.value = false
                    showMore.value = true
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Sort,
                        contentDescription = ""
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Name") },
                onClick = {
                    showSort.value = false
                    libraryViewModel.sort(SortType.Name)
                },
            )
            DropdownMenuItem(
                text = { Text(text = "Date") },
                onClick = {
                    showSort.value = false
                    libraryViewModel.sort(SortType.Date)
                },
            )
        }
    }
}