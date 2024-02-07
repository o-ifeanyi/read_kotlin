package android.ifeanyi.read.app.presentation.views.speech

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightedText(text: String, range: IntRange) {
    val displayedText = remember { mutableStateOf(buildAnnotatedString { }) }

    val wordStyle = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(
        fontWeight = FontWeight.Bold,
        background = MaterialTheme.colorScheme.tertiaryContainer,
    )

    val sentenceStyle = MaterialTheme.typography.bodyMedium.toSpanStyle()

    val spokenStyle = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(
        color = MaterialTheme.colorScheme.outline
    )


    displayedText.value = buildAnnotatedString {
        withStyle(sentenceStyle) {
            append(text)
        }

        addStyle(spokenStyle, start = 0, end = range.first)
        addStyle(wordStyle, start = range.first, end = range.last)
    }

    Text(text = displayedText.value)
}