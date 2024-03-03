package android.ifeanyi.read.app.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun WaveForm(animating: Boolean) {
    val random =  Random(1)

    Row(modifier = Modifier.height(50.dp), verticalAlignment = Alignment.CenterVertically) {
        for (i in 1.. 6) {
            val animation = animateFloatAsState(
                targetValue = if (animating) 1.0f else 0.1f,
                animationSpec = if (animating) infiniteRepeatable(animation = tween(durationMillis = random.nextInt(400, 1000)), repeatMode = RepeatMode.Reverse) else tween(durationMillis = 300),
                label = "Height Animation"
            )
            val height = if (i < 3 || i > 4) random.nextInt(10, 25) else random.nextInt(25, 50)
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                    .height((height * animation.value).dp)
                    .width(4.dp)

            )
        }
    }
}
