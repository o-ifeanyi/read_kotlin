package android.ifeanyi.read.app.presentation.components

import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.theme.AppIcons
import android.ifeanyi.read.core.util.flagEmoji
import android.speech.tts.Voice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSelectorSheet(
    showVoicesSheet: MutableState<Boolean>,
    modalSheetState: SheetState,
    initial: Voice?,
    onDone: (voice: Voice) -> Unit
) {
    val state = SpeechService.state.collectAsState().value

    ModalBottomSheet(
        onDismissRequest = { showVoicesSheet.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(text = "Select Voice", style = MaterialTheme.typography.titleLarge)

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.voices) { voice ->
                    Box {
                        GridTileComponent(
                            asset = {
                                if (voice.locale.flagEmoji != null) Text(
                                    text = voice.locale.flagEmoji!!,
                                    style = MaterialTheme.typography.titleMedium
                                ) else null
                            },
                            tonalElevation = 2.dp,
                            subtitle = voice.locale.displayName,
                            onClick = { onDone.invoke(voice) },
                        )
                        if (voice.name == initial?.name) {
                            Icon(
                                imageVector = AppIcons.checkbox,
                                contentDescription = "",
                                modifier = Modifier.align(
                                    Alignment.TopEnd
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}