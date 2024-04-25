package com.ifeanyi.read.app.presentation.components

import android.annotation.SuppressLint
import com.ifeanyi.read.core.services.SpeechService
import com.ifeanyi.read.core.theme.AppIcons
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerComponent(expanded: Boolean, onClick: () -> Unit) {
    val state = SpeechService.state.collectAsState().value

    AnimatedVisibility(
        visible = state.canPlay,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        ElevatedButton(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(8.dp),
            onClick = { onClick.invoke() }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    WaveForm(animating = !expanded && state.isPlaying)

                    if (state.model != null) {
                        Text(
                            text = state.model.name,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    } else Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        modifier = Modifier.size(35.dp),
                        onClick = { SpeechService.stop(true) },
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = AppIcons.stop,
                            contentDescription = "Stop Button",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            if (state.isPlaying) SpeechService.pause() else SpeechService.play()
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = if (state.isPlaying) AppIcons.pause else AppIcons.play,
                            contentDescription = "Play/Pause Button",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}