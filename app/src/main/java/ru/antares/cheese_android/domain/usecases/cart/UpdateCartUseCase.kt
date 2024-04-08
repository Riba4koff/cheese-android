package ru.antares.cheese_android.domain.usecases.cart

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.room.cart.CartDao
import ru.antares.cheese_android.data.local.room.cart.CartEntity
import ru.antares.cheese_android.data.remote.services.cart.CartProductDTO

/**
 * UpdateCartUseCase.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:27
 * Android studio
 */

class UpdateCartUseCase(
    private val cartDao: CartDao
) {
    suspend fun invoke(products: List<CartProductDTO>) = withContext(Dispatchers.IO) {
        products.forEach { product ->
            cartDao.insert(CartEntity(product.product.id, product.amount))
        }
    }
}