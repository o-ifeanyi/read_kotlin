package android.ifeanyi.read.app.speech

import android.ifeanyi.read.app.common.components.CustomSlider
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.util.flagEmoji
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlagCircle
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen(onCollapse: () -> Unit) {
    val context = LocalContext.current
    val state = SpeechService.state.collectAsState().value
    val displayedText = remember { mutableStateOf(buildAnnotatedString { }) }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

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
                }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                Column(
                    modifier = Modifier.padding(15.dp),
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
                                imageVector = Icons.Rounded.RecordVoiceOver,
                                contentDescription = "Speakers",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(60.dp),
                            onClick = {
                                if (state.isPlaying) SpeechService.pause() else SpeechService.play(
                                    context = context
                                )
                            }
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(60.dp),
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
                                imageVector = Icons.Rounded.Speed,
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
            ModalBottomSheet(
                onDismissRequest = { showVoicesSheet.value = false },
                sheetState = modalSheetState
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(15.dp)
                ) {
                    items(state.voices) { voice ->
                        ElevatedButton(
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(8.dp),
                            onClick = {
                                println("FEATURES: ${voice.name}")
                                coroutineScope.launch {
                                    SpeechService.changeVoice(context, voice)
                                    modalSheetState.hide()
                                }.invokeOnCompletion {
                                    showVoicesSheet.value = false
                                }
                            }) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (voice.locale.flagEmoji != null) Text(
                                    text = voice.locale.flagEmoji!!,
                                    style = MaterialTheme.typography.titleMedium
                                ) else Icon(imageVector = Icons.Rounded.FlagCircle, contentDescription = "Flag")
                                Text(
                                    text = voice.locale.displayName,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showRateSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showRateSheet.value = false },
                sheetState = modalSheetState
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.5f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(15.dp)
                ) {
                    item {
                        CustomSlider(initial = 0.5f) { rate ->
                            coroutineScope.launch {
                                SpeechService.changeRate(context, rate)
                                modalSheetState.hide()
                            }.invokeOnCompletion {
                                showRateSheet.value = false
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 15.dp,
                end = 15.dp,
                bottom = 200.dp
            )
        ) {
            item {
                if (state.canPlay) {
                    val wordStyle = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(
                        fontWeight = FontWeight.Bold,
                        background = MaterialTheme.colorScheme.tertiaryContainer,
                    )

                    val sentenceStyle = MaterialTheme.typography.bodyMedium.toSpanStyle()

                    val spokenStyle = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(
                        color = MaterialTheme.colorScheme.outline
                    )

                    val wordRange = state.wordRange

                    displayedText.value = buildAnnotatedString {
                        withStyle(sentenceStyle) {
                            append(state.text)
                        }

                        addStyle(spokenStyle, start = 0, end = wordRange.first)
                        addStyle(wordStyle, start = wordRange.first, end = wordRange.last)
                    }

                    Text(text = displayedText.value)
                }
            }
        }
    }
}
