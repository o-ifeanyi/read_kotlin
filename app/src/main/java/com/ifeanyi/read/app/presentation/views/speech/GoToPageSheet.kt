package com.ifeanyi.read.app.presentation.views.speech

import com.ifeanyi.read.app.presentation.components.AppButton
import com.ifeanyi.read.app.presentation.components.TextFieldComponent
import com.ifeanyi.read.core.services.AppStateService
import com.ifeanyi.read.core.services.SpeechService
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GoToPageSheet(
    showPageSheet: MutableState<Boolean>,
    modalSheetState: SheetState,
) {
    val focusManager = LocalFocusManager.current

    val text = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    fun onContinue() {
        focusManager.clearFocus()

        coroutineScope.launch {
            try {
                SpeechService.goToPage(text.value.toInt())
            } catch (exc: NumberFormatException) {
                AppStateService.displayMessage("Enter a valid page number")
            }
            modalSheetState.hide()
        }.invokeOnCompletion {
            showPageSheet.value = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = { showPageSheet.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "Go To Page", style = MaterialTheme.typography.titleLarge)

            TextFieldComponent(
                value = text,
                label = { Text("Page") },
                onImeAction = { onContinue() },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            AppButton(
                text = "Continue",
                enabled = text.value.isNotEmpty()
            ) { onContinue() }
        }
    }
}