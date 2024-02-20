package android.ifeanyi.read.app.presentation.views.settings

import android.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import android.ifeanyi.read.core.enums.DisplayStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisplayDialog(
    padding: PaddingValues,
    showDisplay: MutableState<Boolean>,
    settingsVM: SettingsViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding() + 60.dp,
                start = 20.dp, end = 20.dp,
            )
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = showDisplay.value,
            onDismissRequest = {
                showDisplay.value = false
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            DisplayStyle.entries.map {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        showDisplay.value = false
                        settingsVM.setDisplayStyle(it)
                    },
                )
            }
        }
    }
}