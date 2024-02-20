package android.ifeanyi.read.app.presentation.views.settings

import android.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewSheet(
    showWhatsNewSheet: MutableState<Boolean>,
    settingsVM: SettingsViewModel = hiltViewModel()
) {
    val state = settingsVM.state.collectAsState().value
    val modalSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { showWhatsNewSheet.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(text = "What's New", style = MaterialTheme.typography.titleLarge)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(state.whatsNew) { whatsNew ->
                    Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.small) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            Text(
                                text = "🚀 Version ${whatsNew.id}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            whatsNew.features.map {
                                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(text = it.body, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}