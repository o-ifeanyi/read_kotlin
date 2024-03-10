package android.ifeanyi.read.app.presentation.views.settings

import android.ifeanyi.read.app.presentation.components.CustomSliderSheet
import android.ifeanyi.read.app.presentation.components.SettingsItem
import android.ifeanyi.read.app.presentation.components.VoiceSelectorSheet
import android.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.theme.AppIcons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechScreen(settingsVM: SettingsViewModel = hiltViewModel()) {
    val state = settingsVM.state.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    val showVoicesSheet = remember { mutableStateOf(false) }
    val showRateSheet = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Text To Speech") })
        }
    ) { padding ->
        if (showVoicesSheet.value) {
            VoiceSelectorSheet(
                showVoicesSheet = showVoicesSheet,
                modalSheetState = modalSheetState,
                initial = state.voice,
            ) { voice ->
                coroutineScope.launch {
                    settingsVM.setVoice(voice)
                    modalSheetState.hide()
                }.invokeOnCompletion {
                    showVoicesSheet.value = false
                    if (SpeechService.state.value.model != null) {
                        SpeechService.stopAndPlay()
                    }
                }
            }
        }

        if (showRateSheet.value) {
            CustomSliderSheet(
                showRateSheet = showRateSheet,
                modalSheetState = modalSheetState,
                initialProgress = state.speechRate / 2
            ) { rate ->
                coroutineScope.launch {
                    settingsVM.setSpeechRate(rate)
                    modalSheetState.hide()
                }.invokeOnCompletion {
                    showRateSheet.value = false
                    if (SpeechService.state.value.model != null) {
                        SpeechService.stopAndPlay()
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Speaker Voice",
                            icon = AppIcons.speaker,
                            color = Color(0xFFFF9E08),
                            trailing = {
                                Text(text = state.voice?.locale?.displayCountry ?: "")
                            }
                        ) {
                            showVoicesSheet.value = true
                        }
                        SettingsItem(
                            title = "Speech Rate",
                            icon = AppIcons.speechRate,
                            color = Color(0xFF0F85FF),
                            trailing = { Text(text = "${state.speechRate}") }
                        ) {
                            showRateSheet.value = true
                        }
                    }
                }
            }
        }
    }
}