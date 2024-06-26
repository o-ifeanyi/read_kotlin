package com.ifeanyi.read.app.presentation.views.home

import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.LibraryType
import com.ifeanyi.read.app.presentation.components.AppButton
import com.ifeanyi.read.app.presentation.components.TextFieldComponent
import com.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import com.ifeanyi.read.core.services.AnalyticService
import com.ifeanyi.read.core.services.SpeechService
import com.ifeanyi.read.core.util.trimUrl
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EnterUrlSheet(
    showUrlSheet: MutableState<Boolean>,
    libraryVM: LibraryViewModel,
) {
    val focusManager = LocalFocusManager.current

    val url = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    fun onContinue() {
        focusManager.clearFocus()
        val model = FileModel(
            name = url.value.trimUrl,
            type = LibraryType.Url,
            path = url.value,
        )
        AnalyticService.track("enter_url")
        coroutineScope.launch {
            SpeechService.updateModel(model) {
                libraryVM.insertItem(it)
            }
            modalSheetState.hide()
        }.invokeOnCompletion {
            showUrlSheet.value = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = { showUrlSheet.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Enter link", style = MaterialTheme.typography.titleLarge)

            TextFieldComponent(
                value = url,
                label = { Text("Link") },
                onImeAction = { onContinue() }
            )

            AppButton(
                text = "Continue",
                enabled = url.value.isNotEmpty()
            ) { onContinue() }
        }
    }
}