package android.ifeanyi.read.core.services

import android.content.Context
import android.ifeanyi.read.core.enums.AppTheme
import android.ifeanyi.read.core.enums.DisplayStyle
import android.ifeanyi.read.core.util.Constants
import android.speech.tts.Voice
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class PreferenceService(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "store")
    }

    fun getString(key: String): Flow<String?> {
        return context.dataStore.data.map { pref ->
            pref[stringPreferencesKey(key)]
        }
    }

    suspend fun saveString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getVoice(): Flow<Voice> {
        return context.dataStore.data.map { pref ->
            val items = pref[stringPreferencesKey(Constants.voice)]?.split("/") ?: listOf("en-us-x-tpf-local", "en", "US")
            Voice(items[0], Locale(items[1], items[2]), 400, 200, false, emptySet())
        }
    }

    fun getTheme(): Flow<AppTheme> {
        return context.dataStore.data.map { pref ->
            AppTheme.valueOf(pref[stringPreferencesKey(Constants.theme)] ?: "System")
        }
    }

    fun getDisplayStyle(): Flow<DisplayStyle> {
        return context.dataStore.data.map { pref ->
            DisplayStyle.valueOf(pref[stringPreferencesKey(Constants.displayStyle)] ?: "Grid")
        }
    }
}