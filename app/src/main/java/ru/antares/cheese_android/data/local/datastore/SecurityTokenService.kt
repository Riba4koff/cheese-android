package ru.antares.cheese_android.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface SecurityTokenService {
    val authorized: Flow<AuthorizedState>

    suspend fun authorize(token: String)
    suspend fun logout()
}