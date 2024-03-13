package ru.antares.cheese_android.data.local.datastore.token

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
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

class AuthorizationDataStore(context: Context) : IAuthorizationDataStore {

    private companion object KEYS {
        val bearerToken = stringPreferencesKey("BEARER_TOKEN_KEY")
        var authorizationSkipped = booleanPreferencesKey("AUTHORIZATION_SKIPPED")
    }

    private val dataStore = context.appDataStore

    override suspend fun getToken(): String = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.d(TAG, "Can't read preferences: ${exception.localizedMessage}")
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[bearerToken] ?: ""
    }.first()

    override val authorizedState: Flow<AuthorizedState> = dataStore.data.map { preferences ->
        val authorizationSkipped = preferences[authorizationSkipped] ?: false
        preferences[bearerToken].let { token ->
            if (token.isNullOrEmpty().not()) AuthorizedState.AUTHORIZED
            else if (authorizationSkipped) AuthorizedState.SKIPPED
            else AuthorizedState.NOT_AUTHORIZED
        }
    }

    override suspend fun authorize(token: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[bearerToken] = token
                preferences[authorizationSkipped] = false
            }
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[bearerToken] = ""
            }
        }
    }

    override suspend fun skipAuthorization() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[authorizationSkipped] = true
            }
        }
    }
}