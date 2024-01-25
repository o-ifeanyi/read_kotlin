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

data class SpeechState(val text: String? = null, val isPlaying: Boolean = false) {
    var canPlay: Boolean = text != null
}

object SpeechService: ViewModel() {
    private val _state = MutableStateFlow(SpeechState())
    val state = _state.asStateFlow()

    private  var  textToSpeech: TextToSpeech? = null

    fun updateText(text: String?) = viewModelScope.launch {
        _state.update { it.copy(text = text) }
    }

    fun stop() {
        textToSpeech?.stop()
        _state.update { it.copy(isPlaying = false) }
    }

    fun play(context: Context) = viewModelScope.launch {
        textToSpeech = TextToSpeech(
            context
        ) { res ->
            if (res == TextToSpeech.SUCCESS) {
                textToSpeech?.let { speaker ->
                    speaker.language = Locale.US
                    speaker.setSpeechRate(1.0f)
                    speaker.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _state.update { it.copy(isPlaying = true) }
                        }

                        override fun onDone(utteranceId: String?) {
                            _state.update { it.copy(isPlaying = false) }
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _state.update { it.copy(isPlaying = false) }
                        }
                    })
                    speaker.speak(
                        _state.value.text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "utteranceId" // required for listener to work
                    )
                }
            }
        }
    }
}
