package com.ifeanyi.read.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSliderSheet(
    showRateSheet: MutableState<Boolean>,
    modalSheetState: SheetState,
    initialProgress: Float,
    onDone: (result: Float) -> Unit
) {
    val progress = remember { mutableFloatStateOf(initialProgress) }
    val position = animateFloatAsState(
        targetValue = progress.floatValue,
        animationSpec = spring(),
        label = ""
    )
    val size = remember { mutableStateOf(IntSize.Zero) }

    ModalBottomSheet(
        onDismissRequest = { showRateSheet.value = false },
        sheetState = modalSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(20.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Speech Rate", style = MaterialTheme.typography.titleLarge)

            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "slow")
                    Text(text = "normal")
                    Text(text = "fast")
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        size.value = it.size
                    }
                    .clip(shape = MaterialTheme.shapes.small)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { change, _ ->
                                val pos = min(change.position.x, size.value.width.toFloat()) / size.value.width
                                progress.floatValue = max(0.05f, pos)
                            },
                            onDragEnd = {
                                onDone(progress.floatValue * 2)
                            }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.inversePrimary)
                    )

                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(position.value)
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Box(modifier = Modifier.height(60.dp))
        }
    }
}