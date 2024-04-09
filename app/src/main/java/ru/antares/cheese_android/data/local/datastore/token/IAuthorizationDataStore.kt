package ru.antares.cheese_android.data.local.datastore.token

import kotlinx.coroutines.flow.Flow

interface IAuthorizationDataStore {
    val userAuthorizationState: Flow<UserAuthorizationState>

    suspend fun getToken(): String
    suspend fun authorize(token: String)
    suspend fun logout()
    suspend fun skipAuthorization()
}