package android.ifeanyi.read.core.services

import android.content.Context
import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.LibraryType
import android.ifeanyi.read.core.util.Constants
import android.ifeanyi.read.core.util.TextParser
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class SpeechState(
    val text: String = "",
    val model: FileModel? = null,
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

    private lateinit var _appContext: Context
    private lateinit var _updateModel: (fileModel: FileModel) -> Unit

    private var _textToSpeech: TextToSpeech? = null

    private var _words: List<String> = emptyList()
    private var _wordIndex = 0
    private var _textCount: Int = 0



    fun initSpeechService(context: Context, update: (fileModel: FileModel) -> Unit) {
        _appContext = context
        _updateModel = update
        var tts: TextToSpeech? = null
        tts = TextToSpeech(
            context
        ) { res ->
            if (res == TextToSpeech.SUCCESS) {
                _state.update { it.copy(voices = tts!!.voices.toList()) }
                tts!!.shutdown()
            }
        }
    }

    fun updateModel(fileModel: FileModel) = viewModelScope.launch {
        _state.update { it.copy(model = fileModel) }
        when (fileModel.type) {
            LibraryType.Pdf -> {
                val uri = Uri.parse(fileModel.path)
                TextParser.parsePdf(_appContext, uri, fileModel.currentPage) { result, pageCount ->
                    _state.update { it.copy(model = fileModel.copy(totalPages = pageCount)) }
                    _updateModel.invoke(_state.value.model!!)

                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _wordIndex = fileModel.wordIndex
                    _state.update { it.copy(text = result) }
                    play()
                }
            }

            LibraryType.Img -> {
                val uri = Uri.parse(fileModel.path)
                TextParser.parseImage(_appContext, uri) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _wordIndex = fileModel.wordIndex
                    _state.update { it.copy(text = result) }
                    play()
                }
            }

            LibraryType.Txt -> {
                val uri = Uri.parse(fileModel.path)
                TextParser.parseText(uri) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _wordIndex = fileModel.wordIndex
                    _state.update { it.copy(text = result) }
                    play()
                }
            }

            LibraryType.Url -> {
                TextParser.parseUrl(fileModel.path) { result ->
                    stop()
                    _textCount = result.length
                    _words = result.split(" ")
                    _wordIndex = fileModel.wordIndex
                    _state.update { it.copy(text = result) }
                    play()
                }
            }
        }
    }

    fun pause() {
        _textToSpeech?.stop()
    }

    fun forward() {
        _textToSpeech?.stop()
        if (_wordIndex + 10 < _words.size) {
            _wordIndex += 10
        } else {
            _wordIndex = _words.size - 3
        }
        updateProgress()
        play()
    }

    fun rewind() {
        _textToSpeech?.stop()
        if (_wordIndex - 10 > 0) {
            _wordIndex -= 10
        } else {
            _wordIndex = 0
        }
        updateProgress()
        play()
    }

    private fun stop() {
        _wordIndex = 0
        _state.update { it.copy(wordRange = IntRange(0, 0), progress = 0f) }
        _textToSpeech?.stop()
    }

    fun nextPage() {
        val model = _state.value.model ?: return
        if (model.currentPage + 1 > model.totalPages) return
        stop()
        _state.update {
            it.copy(
                model = model.copy(
                    currentPage = model.currentPage + 1,
                    wordIndex = 0,
                    wordRange = IntRange(0, 0)
                )
            )
        }
        updateModel(_state.value.model!!)
    }

    fun prevPage() {
        val model = _state.value.model ?: return
        if (model.currentPage - 1 < 1) return
        stop()
        _state.update {
            it.copy(
                model = model.copy(
                    currentPage = model.currentPage - 1,
                    wordIndex = 0,
                    wordRange = IntRange(0, 0)
                )
            )
        }
        updateModel(_state.value.model!!)
    }

    fun goToPage(page: Int) {
        val model = _state.value.model ?: return
        if (page < 1 || page > model.totalPages) return
        stop()
        _state.update {
            it.copy(
                model = model.copy(
                    currentPage = page, wordIndex = 0,
                    wordRange = IntRange(0, 0)
                )
            )
        }
        updateModel(_state.value.model!!)
    }

    fun play() = viewModelScope.launch {
        val pref = PreferenceService(context = _appContext)
        val voice = pref.getVoice().first()
        val speechRate = pref.getString(Constants.speechRate).first()
        _textToSpeech = TextToSpeech(
            _appContext
        ) { res ->
            if (res == TextToSpeech.SUCCESS) {
                _textToSpeech?.let { speaker ->
                    if (_state.value.voices.isEmpty()) {
                        _state.update { it.copy(voices = speaker.voices.toList()) }
                    }

                    speaker.language = Locale.US
                    speaker.setSpeechRate((speechRate ?: "1.0").toFloat())
                    speaker.setVoice(voice)
                    speaker.setOnUtteranceProgressListener(ProgressListener())

                    val model = _state.value.model ?: return@let

                    speaker.speak(
                        _state.value.text.take(4000).substring(model.wordRange.first),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "utteranceId" // required for listener to work
                    )
                }
            }
        }
    }

    fun stopAndPlay() {
        _textToSpeech?.stop()
        play()
    }

    private fun updateProgress() {
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

                val model = _state.value.model ?: return
                _state.update {
                    it.copy(
                        model = model.copy(
                            progress = (progress * 100).toInt(),
                            wordRange = range,
                            wordIndex = _wordIndex - 1,
                        )
                    )
                }
                _updateModel.invoke(_state.value.model!!)
            }

        }
    }

    class ProgressListener : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            _state.update { it.copy(isPlaying = true) }
            viewModelScope.launch {
                delay(200)
                NotificationService.showMediaStyleNotification()
            }
        }

        override fun onDone(utteranceId: String?) {
            _state.update { it.copy(isPlaying = false) }
            viewModelScope.launch {
                delay(200)
                NotificationService.showMediaStyleNotification()
            }
            nextPage()
        }

        override fun onRangeStart(
            utteranceId: String?,
            start: Int,
            end: Int,
            frame: Int
        ) {

            if (_wordIndex < _words.size) {
                updateProgress()
                viewModelScope.launch {
                    NotificationService.showMediaStyleNotification()
                }
            }
        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            _state.update { it.copy(isPlaying = false) }
            viewModelScope.launch {
                delay(200)
                NotificationService.showMediaStyleNotification()
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            _state.update { it.copy(isPlaying = false) }
            viewModelScope.launch {
                delay(200)
                NotificationService.showMediaStyleNotification()
            }
        }
    }
}

