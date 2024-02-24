package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.services.cart.BasketResponse
import ru.antares.cheese_android.data.remote.services.cart.UpdateCartRequest
import ru.antares.cheese_android.domain.ResourceState

/**
 * ICartRepository.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:25
 * Android studio
 */

interface ICartRepository {
    suspend fun updateLocalCart(): Unit
    suspend fun get(): Flow<ResourceState<BasketResponse>>
    suspend fun increment(currentAmount: Int, productID: String): Flow<ResourceState<Boolean>>
    suspend fun decrement(currentAmount: Int, productID: String): Flow<ResourceState<Boolean>>
    suspend fun clear(productID: String): Flow<ResourceState<Boolean>>
}