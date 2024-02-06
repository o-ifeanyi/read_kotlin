package android.ifeanyi.read.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TileButtonComponent(
    modifier: Modifier = Modifier,
    asset: @Composable (() -> Unit?)? = null,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(10.dp),
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            asset?.invoke()
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = subtitle)
            }
        }
    }
}