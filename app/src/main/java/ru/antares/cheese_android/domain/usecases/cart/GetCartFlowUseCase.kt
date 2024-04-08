package ru.antares.cheese_android.domain.usecases.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.antares.cheese_android.data.local.room.cart.CartDao
import ru.antares.cheese_android.data.local.room.cart.CartEntity
import ru.antares.cheese_android.data.local.room.products.ProductsLocalStorage
import ru.antares.cheese_android.domain.models.CartProductModel

/**
 * GetCartFlowUseCase.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:23
 * Android studio
 */

class GetCartFlowUseCase(
    private val cartDao: CartDao,
    private val productsLocalStorage: ProductsLocalStorage
) {
    val entitites: Flow<List<CartEntity>> = cartDao.subscribeCartFlow()

    val products: Flow<List<CartProductModel>> =
        cartDao.subscribeCartFlow().combine(productsLocalStorage.products()) { cart, products ->
            products.mapNotNull { product ->
                val amount = cart.find { it.productID == product.id }?.amount
                amount?.let {
                    CartProductModel(
                        amount = it,
                        price = product.price,
                        priceWithDiscount = null,
                        product = product
                    )
                }
            }
        }
}