package android.ifeanyi.read.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun CustomSlider(initial: Float, onDone: (result: Float) -> Unit) {
    val progress = remember { mutableFloatStateOf(initial) }
    val position = animateFloatAsState(
        targetValue = progress.floatValue,
        animationSpec = spring(),
        label = ""
    )


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "x0.1")
            Text(text = "x1.0")
            Text(text = "x2.0")
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, _ ->
                        progress.floatValue = max(0.05f, change.position.x / 1000)
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
}