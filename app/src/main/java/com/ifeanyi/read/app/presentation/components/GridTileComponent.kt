package com.ifeanyi.read.app.presentation.components

import com.ifeanyi.read.core.theme.AppIcons
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridTileComponent(
    modifier: Modifier = Modifier,
    asset: @Composable (() -> Unit?)? = null,
    title: String? = null,
    subtitle: String,
    tonalElevation: Dp = 1.dp,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = { onLongPress?.invoke() }
            ),
        shape = MaterialTheme.shapes.small,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (asset?.invoke() == null) Icon(
                imageVector = AppIcons.flag,
                contentDescription = "Flag",
                modifier = Modifier.size(30.dp)
            )
            if (title != null) Text(
                text = title, maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = subtitle,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}