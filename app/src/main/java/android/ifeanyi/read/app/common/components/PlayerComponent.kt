package android.ifeanyi.read.app.common.components

import android.annotation.SuppressLint
import android.ifeanyi.read.app.speech.SpeechScreen
import android.ifeanyi.read.core.services.SpeechService
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerComponent() {
    val context = LocalContext.current
    val state = SpeechService.state.collectAsState().value

    var expanded by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = expanded,
        label = "Animated Player",
        transitionSpec = {
            slideInVertically(animationSpec = tween(300), initialOffsetY = {0}) togetherWith
                    slideOutVertically(animationSpec = tween(300))
        }
    ) { targetExpanded ->
        if (targetExpanded) {
            SpeechScreen {
                expanded = !expanded
            }
        } else {
            AnimatedVisibility(
                visible = state.canPlay,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                ElevatedButton(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    contentPadding = PaddingValues(8.dp),
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.inversePrimary,
                                        shape = MaterialTheme.shapes.small
                                    ),
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    SpeechService.stop()
                                },
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = Icons.Rounded.Stop,
                                    contentDescription = "Stop Button",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (state.isPlaying) SpeechService.pause() else SpeechService.play(
                                        context = context
                                    )
                                },
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
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

    }


}