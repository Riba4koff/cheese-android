package ru.antares.cheese_android.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TAG = "TOKEN_SERVICE"

class TokenService(context: Context) : SecurityTokenService {

    private companion object KEYS {
        val BearerToken = stringPreferencesKey("BEARER_TOKEN_KEY")
    }

    private val dataStore = context.appDataStore

    override val authorized: Flow<AuthorizedState> = dataStore.data.map { preferences ->
        preferences[BearerToken].let { token ->
            if (token.isNullOrEmpty()) AuthorizedState.NOT_AUTHORIZED
            else AuthorizedState.AUTHORIZED
        }
    }

    override suspend fun authorize(token: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[BearerToken] = token
            }
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[BearerToken] = ""
            }
        }
    }
}