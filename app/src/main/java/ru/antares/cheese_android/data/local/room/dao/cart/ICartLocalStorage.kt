package ru.antares.cheese_android.data.local.room.dao.cart

import kotlinx.coroutines.flow.Flow

/**
 * ICartLocalStorage.kt
 * @author Павел
 * Created by on 23.02.2024 at 17:12
 * Android studio
 */

interface ICartLocalStorage {
    val cart: Flow<List<CartEntity>>

    suspend fun insert(entity: CartEntity)
    suspend fun increment(amount: Int, productID: String)
    suspend fun decrement(amount: Int, productID: String)
    suspend fun delete(productID: String)
    suspend fun clear()
}

class CartLocalStorage(
    private val dao: CartDao
):ICartLocalStorage {
    override val cart: Flow<List<CartEntity>> = dao.subscribeCartFlow()

    override suspend fun insert(entity: CartEntity) {
        dao.insert(entity)
    }

    override suspend fun increment(amount: Int, productID: String) {
        dao.insert(CartEntity(productID,amount + 1))
    }

    override suspend fun decrement(amount: Int, productID: String) {
        dao.update(amount - 1, productID)
    }

    override suspend fun delete(productID: String) {
        dao.delete(productID)
    }

    override suspend fun clear() {
        dao.clear()
    }
}