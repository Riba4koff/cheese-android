package ru.antares.cheese_android.data.local.datastore.token

import kotlinx.coroutines.flow.Flow

interface ITokenService {
    val authorizedState: Flow<AuthorizedState>

    suspend fun getToken(): String
    suspend fun authorize(token: String)
    suspend fun logout()
    suspend fun skipAuthorization()
}