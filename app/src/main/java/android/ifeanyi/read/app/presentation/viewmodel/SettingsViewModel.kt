package android.ifeanyi.read.app.presentation.viewmodel

import android.ifeanyi.read.app.data.SettingsRepository
import android.ifeanyi.read.app.data.models.WhatsNewModel
import android.ifeanyi.read.app.data.models.latestUpdate
import android.ifeanyi.read.core.enums.AppTheme
import android.ifeanyi.read.core.enums.DisplayStyle
import android.ifeanyi.read.core.services.PreferenceService
import android.ifeanyi.read.core.util.Constants
import android.speech.tts.Voice
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val theme: AppTheme = AppTheme.System,
    val displayStyle: DisplayStyle = DisplayStyle.Grid,
    val speechRate: Float = 1.0f,
    val voice: Voice? = null,
    val whatsNew: List<WhatsNewModel> = emptyList(),
    val showWhatsNew: MutableState<Boolean> = mutableStateOf(false)
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val preferenceService: PreferenceService
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        getTheme()
        checkWhatsNew()
        getAllUpdates()
        getDisplayStyle()
        getSpeechRate()
        getVoice()
    }

    private fun getTheme() = viewModelScope.launch {
        preferenceService.getTheme().distinctUntilChanged().collect { theme ->
            _state.update { it.copy(theme = theme) }
        }
    }

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        preferenceService.saveString(key = Constants.theme, value = theme.name)
    }

    private fun checkWhatsNew() = viewModelScope.launch {
        val latest = settingsRepository.getUpdate(latestUpdate.id)
        if (latest == null) {
            settingsRepository.insertItem(latestUpdate)
            _state.update { it.copy(showWhatsNew = mutableStateOf(true)) }
        }
    }

    private fun getAllUpdates() = viewModelScope.launch {
        settingsRepository.getAllUpdates().distinctUntilChanged().collect { whatsNew ->
            _state.update { it.copy(whatsNew = whatsNew.reversed()) }
        }
    }

    private fun getDisplayStyle() = viewModelScope.launch {
        preferenceService.getDisplayStyle().distinctUntilChanged().collect { style ->
            _state.update { it.copy(displayStyle = style) }
        }
    }

    fun setDisplayStyle(style: DisplayStyle) = viewModelScope.launch {
        preferenceService.saveString(key = Constants.displayStyle, value = style.name)
    }

    private fun getSpeechRate() = viewModelScope.launch {
        preferenceService.getString(Constants.speechRate).distinctUntilChanged().collect { rate ->
            val speechRate = (rate ?: "1.0").toDouble()
            _state.update { it.copy(speechRate = String.format("%.1f", speechRate).toFloat()) }
        }
    }

    fun setSpeechRate(rate: Float) = viewModelScope.launch {
        preferenceService.saveString(key = Constants.speechRate, value = "$rate")
    }

    private fun getVoice() = viewModelScope.launch {
        preferenceService.getVoice().distinctUntilChanged().collect { voice ->
            _state.update { it.copy(voice = voice) }
        }
    }

    fun setVoice(voice: Voice) = viewModelScope.launch {
        preferenceService.saveString(
            key = Constants.voice,
            value = "${voice.name}/${voice.locale.language}/${voice.locale.country}"
        )
    }
}