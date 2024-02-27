package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.dao.cart.CartDao
import ru.antares.cheese_android.data.local.room.dao.cart.CartEntity
import ru.antares.cheese_android.data.local.room.dao.cart.ICartLocalStorage
import ru.antares.cheese_android.data.remote.services.cart.BasketResponse
import ru.antares.cheese_android.data.remote.services.cart.CartService
import ru.antares.cheese_android.data.remote.services.cart.UpdateCartRequest
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartUIError

/**
 * CartRepository.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:34
 * Android studio
 */

class CartRepository(
    private val cartService: CartService,
    private val cartLocalStorage: ICartLocalStorage
) : ICartRepository {
    override suspend fun updateLocalCart() {
        safeNetworkCall { cartService.get() }.onSuccess { response ->
            response.result.forEach { cartProductDTO ->
                cartLocalStorage.insert(
                    entity = CartEntity(
                        amount = cartProductDTO.amount,
                        productID = cartProductDTO.product.id
                    )
                )
            }
        }
    }

    override suspend fun get(): Flow<ResourceState<BasketResponse>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            safeNetworkCall { cartService.get() }.onFailure { error ->
                if (error.code == 401) {
                    emit(ResourceState.Error(CartUIError.UnauthorizedError()))
                } else {
                    emit(ResourceState.Error(CartUIError.LoadCartError()))
                }
                return@onFailure
            }.onSuccess { response ->
                emit(ResourceState.Success(response))
                return@onSuccess
            }

            emit(ResourceState.Loading(isLoading = false))
        }

    override suspend fun increment(
        currentAmount: Int,
        productID: String
    ): Flow<ResourceState<Boolean>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            safeNetworkCall {
                cartService.update(
                    request = UpdateCartRequest(
                        productID = productID,
                        amount = currentAmount + 1
                    )
                )
            }.onFailure { error ->
                if (error.code == 401) {
                    emit(ResourceState.Error(CartUIError.UnauthorizedError()))
                } else {
                    emit(ResourceState.Error(CartUIError.IncrementProductError()))
                }
                return@onFailure
            }.onSuccess { success ->
                if (success) {
                    cartLocalStorage.increment(currentAmount, productID)
                    emit(ResourceState.Success(success))
                } else {
                    emit(ResourceState.Error(CartUIError.IncrementProductError()))
                }
                return@onSuccess
            }

            emit(ResourceState.Loading(isLoading = false))
        }

    override suspend fun decrement(
        currentAmount: Int,
        productID: String
    ): Flow<ResourceState<Boolean>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            if (currentAmount > 1) {
                safeNetworkCall {
                    cartService.update(
                        request = UpdateCartRequest(
                            productID = productID,
                            amount = currentAmount - 1
                        )
                    )
                }.onFailure { error ->
                    if (error.code == 401) {
                        emit(ResourceState.Error(CartUIError.UnauthorizedError()))
                    } else {
                        emit(ResourceState.Error(CartUIError.DecrementProductError()))
                    }
                    return@onFailure
                }.onSuccess { success ->
                    if (success) {
                        cartLocalStorage.decrement(currentAmount, productID)
                        emit(ResourceState.Success(success))
                    } else {
                        emit(ResourceState.Error(CartUIError.DecrementProductError()))
                    }
                    return@onSuccess
                }
            } else {
                safeNetworkCall {
                    cartService.delete(productID = productID)
                }.onFailure { error ->
                    if (error.code == 401) {
                        emit(ResourceState.Error(CartUIError.UnauthorizedError()))
                    } else {
                        emit(ResourceState.Error(CartUIError.DeleteProductError()))
                    }
                    return@onFailure
                }.onSuccess { success ->
                    if (success) {
                        cartLocalStorage.delete(productID)
                        emit(ResourceState.Success(success))
                    } else {
                        emit(ResourceState.Error(CartUIError.DeleteProductError()))
                    }
                    return@onSuccess
                }
            }

            emit(ResourceState.Loading(isLoading = false))
        }

    override suspend fun delete(productID: String): Flow<ResourceState<Boolean>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCall {
            cartService.delete(productID = productID)
        }.onFailure { error ->
            if (error.code == 401) {
                emit(ResourceState.Error(CartUIError.UnauthorizedError()))
            } else {
                emit(ResourceState.Error(CartUIError.DeleteProductError()))
            }
            return@onFailure
        }.onSuccess { success ->
            if (success) {
                cartLocalStorage.delete(productID)
                emit(ResourceState.Success(success))
            } else {
                emit(ResourceState.Error(CartUIError.DeleteProductError()))
            }
            return@onSuccess
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    override suspend fun clear(productID: String): Flow<ResourceState<Boolean>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            safeNetworkCall { cartService.clear() }.onFailure { error ->
                Log.d("CLEAR_CART_ERROR", error.message)
                emit(ResourceState.Error(CartUIError.ClearError()))
                return@onFailure
            }.onSuccess { success ->
                if (success) {
                    cartLocalStorage.clear()
                    emit(ResourceState.Success(success))
                } else {
                    emit(ResourceState.Error(CartUIError.ClearError()))
                }
                return@onSuccess
            }

            emit(ResourceState.Loading(isLoading = true))
        }
}