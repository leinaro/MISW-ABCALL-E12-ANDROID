package com.misw.abcall.data.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.internal.wait
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val SELECTED_LANGUAGE = stringPreferencesKey("selected_langauge")

    fun getSelectedLanguage(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[SELECTED_LANGUAGE]
            }
    }

    suspend fun updateSelectedLanguage(code: String) {
        context.dataStore.edit { settings ->
            settings[SELECTED_LANGUAGE] = code
        }
    }
}