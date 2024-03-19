package android.ifeanyi.read.app.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TextFieldComponent(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    onValueChange: ((String) -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onImeAction: (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
    textLimit: Int? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focus = remember { FocusRequester() }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focus),
        value = value.value,
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = {
            value.value = it.take(textLimit ?: it.length)
            onValueChange?.invoke(it)
        },
        supportingText = supportingText,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = keyboardActions ?: KeyboardActions(
            onSearch = {
                onImeAction?.invoke()
                keyboardController?.hide()
            },
            onGo = {
                onImeAction?.invoke()
                keyboardController?.hide()
            },
            onDone = {
                focus.freeFocus()
                keyboardController?.hide()
            }
        ),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}