package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.presentation.components.GridButtonComponent
import android.ifeanyi.read.app.presentation.components.TextFieldComponent
import android.ifeanyi.read.app.presentation.components.TileButtonComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.util.dwdm
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

enum class ListStyle { List, Grid }

@Composable
fun LibraryScreen(libraryViewModel: LibraryViewModel) {
    val context = LocalContext.current
    val locale = Locale.getDefault()
    val state = libraryViewModel.state.collectAsState().value

    val searchText = remember { mutableStateOf("") }

    val isSelecting = remember { mutableStateOf(false) }
    val selected = remember { mutableStateListOf<LibraryModel>() }

    val allSelected = remember(selected.size) {
        selected.size == state.items.size
    }

    val showMore = remember { mutableStateOf(false) }
    val showSort = remember { mutableStateOf(false) }

    val listStyle = remember { mutableStateOf(ListStyle.Grid) }

     fun onItemClick(model: LibraryModel) {
        if (isSelecting.value) {
            if (selected.contains(model)) {
                selected.remove(model)
            } else {
                selected.add(model)
            }
        } else {
            SpeechService.updateModel(context, model)
        }
    }

    Scaffold(
        topBar = {
            LibraryTopBar(
                isSelecting, selected, allSelected, state, libraryViewModel, showMore
            )
        }
    ) { padding ->
        LibraryMoreDialog(padding, showMore, showSort, isSelecting, listStyle)
        LibrarySortDialog(padding, showSort, showMore)
        Column(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                start = 15.dp, end = 15.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextFieldComponent(value = searchText)

            when (listStyle.value) {
                ListStyle.Grid -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 200.dp)
                    ) {
                        items(state.items) { model ->
                            Box {
                                GridButtonComponent(
                                    asset = {
                                        Icon(
                                            imageVector = model.icon(),
                                            contentDescription = "Icon",
                                            modifier = Modifier.size(30.dp),
                                        )
                                    },
                                    title = model.name,
                                    subtitle = "${model.type.name.lowercase()} • ${model.progress}%\n${model.date.dwdm(locale)}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    isSelecting.value = !isSelecting.value
                                                }
                                            )
                                        },
                                    onClick = { onItemClick(model) }
                                )
                                if (isSelecting.value && selected.contains(model)) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckBox,
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

                ListStyle.List -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(bottom = 200.dp)
                    ) {
                        items(state.items) { model ->
                            Box {
                                TileButtonComponent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterStart),
                                    asset = {
                                        Icon(
                                            imageVector = model.icon(),
                                            contentDescription = "Icon",
                                            modifier = Modifier.size(40.dp),
                                        )
                                    },
                                    title = model.name,
                                    subtitle = "${model.type.name.lowercase()} • ${model.progress}% • ${model.date.dwdm(locale)}",
                                    onClick = { onItemClick(model) }
                                )
                                if (isSelecting.value && selected.contains(model)) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckBox,
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
