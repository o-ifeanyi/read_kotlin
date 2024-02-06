package android.ifeanyi.read.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlagCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GridButtonComponent(
    modifier: Modifier = Modifier,
    asset: @Composable (() -> Unit?)? = null,
    title: String? = null,
    subtitle: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.elevatedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (asset?.invoke() == null) Icon(
                imageVector = Icons.Rounded.FlagCircle,
                contentDescription = "Flag",
                modifier = Modifier.size(30.dp)
            )
            if (title != null) Text(
                text = title, maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}