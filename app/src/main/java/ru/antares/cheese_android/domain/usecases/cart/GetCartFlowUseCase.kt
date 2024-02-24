package ru.antares.cheese_android.domain.usecases.cart

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.local.room.dao.cart.CartDao
import ru.antares.cheese_android.data.local.room.dao.cart.CartEntity

/**
 * GetCartFlowUseCase.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:23
 * Android studio
 */

class GetCartFlowUseCase(
    private val cartDao: CartDao
) {
    val value: Flow<List<CartEntity>> = cartDao.subscribeCartFlow()
}