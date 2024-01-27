package android.ifeanyi.read.core.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class SpeechState(
    val text: String = "",
    val isPlaying: Boolean = false,
    val wordRange: IntRange = IntRange(0, 0),
    val progress: Float = 0f
) {
    var canPlay: Boolean = text.isNotEmpty()
}

object SpeechService : ViewModel() {
    private val _state = MutableStateFlow(SpeechState())
    val state = _state.asStateFlow()

    private var _textToSpeech: TextToSpeech? = null

    private var _textCount: Int = 0

    fun updateText(text: String) = viewModelScope.launch {
        _state.update { it.copy(text = text) }
    }

    fun stop() {
        _textToSpeech?.stop()
    }

    fun play(context: Context) = viewModelScope.launch {
        _textToSpeech = TextToSpeech(
            context
        ) { res ->
            if (res == TextToSpeech.SUCCESS) {
                _textCount = _state.value.text.length
                println(_textCount)
                _textToSpeech?.let { speaker ->
                    speaker.language = Locale.US
                    speaker.setSpeechRate(1.2f)
                    speaker.setOnUtteranceProgressListener(ProgressListener())
                    speaker.speak(
                        _state.value.text.take(4000),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "utteranceId" // required for listener to work
                    )
                }
            }
        }
    }

    class ProgressListener : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            _state.update { it.copy(isPlaying = true) }
        }

        override fun onDone(utteranceId: String?) {
            _state.update { it.copy(isPlaying = false, wordRange = IntRange(0, 0), progress = 0f) }
        }

        override fun onRangeStart(
            utteranceId: String?,
            start: Int,
            end: Int,
            frame: Int
        ) {
            val progress = end / _textCount.toFloat()
            _state.update { it.copy(wordRange = IntRange(start, end), progress = progress) }

        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            _state.update { it.copy(isPlaying = false, wordRange = IntRange(0, 0), progress = 0f) }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            _state.update { it.copy(isPlaying = false) }
        }
    }
}

