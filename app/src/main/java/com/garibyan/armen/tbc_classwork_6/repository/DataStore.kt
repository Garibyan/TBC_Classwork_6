package com.garibyan.armen.tbc_classwork_6.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCE_NAME = "PREFERENCE_NAME"

class DataStore(private val context: Context) {

    val getToken: Flow<String> = context.dataStore.data
        .map {
            it[KEY_AUTH] ?: ""
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[KEY_AUTH] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            PREFERENCE_NAME
        )
        private val KEY_AUTH = stringPreferencesKey("KEY_AUTH")
    }
}