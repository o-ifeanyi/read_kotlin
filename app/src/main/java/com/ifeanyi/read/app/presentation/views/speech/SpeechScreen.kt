package com.ifeanyi.read.app.presentation.views.speech

import com.ifeanyi.read.app.presentation.components.CustomSliderSheet
import com.ifeanyi.read.app.presentation.components.VoiceSelectorSheet
import com.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import com.ifeanyi.read.core.services.SpeechService
import com.ifeanyi.read.core.theme.AppIcons
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen(settingsVM: SettingsViewModel = hiltViewModel(), onCollapse: () -> Unit) {
    val config = LocalConfiguration.current
    val state = SpeechService.state.collectAsState().value
    val settingState = settingsVM.state.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

    val showPageSheet = remember { mutableStateOf(false) }
    val showVoicesSheet = remember { mutableStateOf(false) }
    val showRateSheet = remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Player") },
                navigationIcon = {
                    BackHandler(enabled = true) {
                        onCollapse.invoke()
                    }
                },
                actions = {
                    if (state.model != null) {
                        val model = state.model
                        TextButton(onClick = {
                            showPageSheet.value = true
                        }) {
                            Text(
                                text = "Page ${model.currentPage} of ${model.totalPages}",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                Column(
                    modifier = Modifier.padding(
                        20.dp, 20.dp, 20.dp,
                        bottom = 30.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier.size(30.dp),
                            onClick = {
                                showVoicesSheet.value = true
                            }
                        )
                        {
                            Icon(
                                imageVector = AppIcons.speaker,
                                contentDescription = "Speakers",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(45.dp),
                            onClick = {
                                SpeechService.rewind()
                            }
                        )
                        {
                            Icon(
                                imageVector = AppIcons.rewind,
                                contentDescription = "Rewind",
                                modifier = Modifier.size(45.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(60.dp),
                            onClick = {
                                if (state.isPlaying) SpeechService.pause() else SpeechService.play()
                            }
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) AppIcons.pause else AppIcons.play,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(45.dp),
                            onClick = {
                                SpeechService.forward()
                            }
                        )
                        {
                            Icon(
                                imageVector = AppIcons.forward,
                                contentDescription = "Forward",
                                modifier = Modifier.size(45.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(30.dp),
                            onClick = {
                                showRateSheet.value = true
                            })
                        {
                            Icon(
                                imageVector = AppIcons.speechRate,
                                contentDescription = "Speed",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (showVoicesSheet.value) {
            VoiceSelectorSheet(
                showVoicesSheet = showVoicesSheet,
                modalSheetState = modalSheetState,
                initial = settingState.voice,
            ) { voice ->
                coroutineScope.launch {
                    settingsVM.setVoice(voice)
                    modalSheetState.hide()
                }.invokeOnCompletion {
                    showVoicesSheet.value = false
                    SpeechService.stopAndPlay()
                }
            }
        }

        if (showRateSheet.value) {
            CustomSliderSheet(
                showRateSheet = showRateSheet,
                modalSheetState = modalSheetState,
                initialProgress = settingState.speechRate / 2,
            ) { rate ->
                coroutineScope.launch {
                    settingsVM.setSpeechRate(rate)
                    modalSheetState.hide()
                }.invokeOnCompletion {
                    showRateSheet.value = false
                    SpeechService.stopAndPlay()
                }
            }
        }

        if (showPageSheet.value) {
            GoToPageSheet(
                showPageSheet = showPageSheet,
                modalSheetState = modalSheetState,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val location = offset.x.toDp().value
                        if (location <= config.screenWidthDp * 0.35) {
                            SpeechService.prevPage()
                        } else if (location >= config.screenWidthDp * 0.65) {
                            SpeechService.nextPage()
                        }
                    }
                },
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            )
        ) {
            item {
                if (state.canPlay) {
                    HighlightedText(text = state.text, range = state.wordRange)
                }
            }
        }
    }
}
