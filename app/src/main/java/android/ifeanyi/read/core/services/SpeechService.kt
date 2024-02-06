package android.ifeanyi.read.core.services

import android.content.Context
import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.data.models.LibraryType
import android.ifeanyi.read.core.util.TextParser
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class SpeechState(
    val text: String = "",
    val model: LibraryModel? = null,
    val isPlaying: Boolean = false,
    val wordRange: IntRange = IntRange(0, 0),
    val progress: Float = 0f,
    val voices: List<Voice> = emptyList()
) {
    var canPlay: Boolean = text.isNotEmpty()
}

object SpeechService : ViewModel() {
    private val _state = MutableStateFlow(SpeechState())
    val state = _state.asStateFlow()

    private var _textToSpeech: TextToSpeech? = null

    private var _words: List<String> = emptyList()
    private var _wordIndex = 0
    private var _textCount: Int = 0

    fun updateModel(context: Context, libraryModel: LibraryModel) = viewModelScope.launch {
        _state.update { it.copy(model = libraryModel) }
        when (libraryModel.type) {
            LibraryType.Pdf -> {
                val uri = Uri.parse(libraryModel.path)
                TextParser.parsePdf(context, uri) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _state.update { it.copy(text = result) }
                    play(context)
                }
            }

            LibraryType.Image -> {
                val uri = Uri.parse(libraryModel.path)
                TextParser.parseImage(context, uri) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _state.update { it.copy(text = result) }
                    play(context)
                }
            }

            LibraryType.Url -> {
                println(libraryModel.name)
                TextParser.parseUrl(libraryModel.path) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _state.update { it.copy(text = result) }
                    play(context)
                }
            }
        }
    }

    fun pause() {
        _textToSpeech?.stop()
    }

    fun stop() {
        _wordIndex = 0
        _state.update { it.copy(wordRange = IntRange(0, 0), progress = 0f) }
        _textToSpeech?.stop()
    }

    fun play(context: Context, voice: Voice? = null, rate: Float? = null) = viewModelScope.launch {
        _textToSpeech = TextToSpeech(
            context
        ) { res ->
            if (res == TextToSpeech.SUCCESS) {
                _textToSpeech?.let { speaker ->
                    _state.update { it.copy(voices = speaker.voices.toList()) }
                    speaker.language = Locale.US
                    speaker.setSpeechRate(rate ?: 1.2f)
                    if (voice != null) {
                        val r = speaker.setVoice(voice)
                        println("SET VOICE RESULT: $r")
                    }
                    speaker.setOnUtteranceProgressListener(ProgressListener())
                    speaker.speak(
                        _state.value.text.take(4000).substring(_state.value.wordRange.first),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "utteranceId" // required for listener to work
                    )
                }
            }
        }
    }

    fun changeVoice(context: Context, voice: Voice) {
        _textToSpeech?.stop()
        play(context, voice = voice)
    }

    fun changeRate(context: Context, rate: Float) {
        _textToSpeech?.stop()
        play(context, rate = rate)
    }

    class ProgressListener : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            _state.update { it.copy(isPlaying = true) }
        }

        override fun onDone(utteranceId: String?) {
            _wordIndex = 0
            _state.update { it.copy(isPlaying = false, wordRange = IntRange(0, 0), progress = 0f) }
        }

        override fun onRangeStart(
            utteranceId: String?,
            start: Int,
            end: Int,
            frame: Int
        ) {

            if (_wordIndex < _words.size) {
                val utterance = _words[_wordIndex]

                val regex = utterance.toRegex()
                val spoken = _words.take(_wordIndex).joinToString(" ").length
                val matchResult = regex.find(input = state.value.text, startIndex = spoken)

                _wordIndex++
                matchResult?.let { match ->
                    if (match.range.last > match.range.first) {
                        val progress = match.range.last / _textCount.toFloat()
                        val range = IntRange(match.range.first, match.range.last + 1)
                        _state.update { it.copy(wordRange = range, progress = progress) }
                    }

                }
            }

        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            _state.update { it.copy(isPlaying = false) }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            _state.update { it.copy(isPlaying = false) }
        }
    }
}

