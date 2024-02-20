package android.ifeanyi.read.app.presentation.views.library

import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.presentation.components.AppButton
import android.ifeanyi.read.app.presentation.components.TextFieldComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
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
fun CreateFolderSheet(
    createFolder: MutableState<Boolean>,
    libraryVM: LibraryViewModel
) {
    val name = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    fun onContinue() {
        val folder = FolderModel(name = name.value)

        coroutineScope.launch {
            libraryVM.insertItem(folder)
            modalSheetState.hide()
        }.invokeOnCompletion {
            createFolder.value = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = { createFolder.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Create Folder", style = MaterialTheme.typography.titleLarge)

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