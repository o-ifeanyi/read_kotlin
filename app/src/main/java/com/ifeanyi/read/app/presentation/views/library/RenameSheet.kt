package com.ifeanyi.read.app.presentation.views.library


import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.FolderModel
import com.ifeanyi.read.app.presentation.components.AppButton
import com.ifeanyi.read.app.presentation.components.TextFieldComponent
import com.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RenameSheet(
    renameItem: MutableState<Boolean>,
    file: FileModel? = null,
    folder: FolderModel? =null,
    libraryVM: LibraryViewModel,
    onDone: () -> Unit,
) {
    val name = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    fun onContinue() {
        coroutineScope.launch {
            if (file != null) {
                libraryVM.updateItem(file.copy(name = name.value))
            } else if (folder != null) {
                libraryVM.updateItem(folder.copy(name = name.value))
            }
            modalSheetState.hide()
        }.invokeOnCompletion {
            onDone.invoke()
        }
    }

    LaunchedEffect(key1 = Unit) {
        name.value = file?.name ?: folder?.name ?: ""
    }

    ModalBottomSheet(
        onDismissRequest = { renameItem.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Rename", style = MaterialTheme.typography.titleLarge)

            TextFieldComponent(
                value = name,
                label = { Text("Name") },
                onImeAction = { onContinue() },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = { onContinue() }
                )
            )

            AppButton(
                text = "Continue",
                enabled = name.value.isNotEmpty()
            ) { onContinue() }
        }
    }
}