package android.ifeanyi.read.app.presentation.views.settings

import android.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import android.ifeanyi.read.core.enums.AppTheme
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
fun ThemeDialog(
    padding: PaddingValues,
    showTheme: MutableState<Boolean>,
    settingsVM: SettingsViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
            )
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = showTheme.value,
            onDismissRequest = {
                showTheme.value = false
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            AppTheme.entries.map {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        showTheme.value = false
                        settingsVM.setTheme(it)
                    },
                )
            }
        }
    }
}