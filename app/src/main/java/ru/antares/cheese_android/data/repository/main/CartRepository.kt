package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.room.cart.CartEntity
import ru.antares.cheese_android.data.local.room.cart.ICartLocalStorage
import ru.antares.cheese_android.data.local.room.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.products.IProductsLocalStorage
import ru.antares.cheese_android.data.remote.api.cart.BasketResponse
import ru.antares.cheese_android.data.remote.api.cart.CartError
import ru.antares.cheese_android.data.remote.api.cart.CartApi
import ru.antares.cheese_android.data.remote.api.cart.CartApiHandler
import ru.antares.cheese_android.data.remote.api.cart.UpdateCartRequest
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.domain.result.CheeseResult
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartAppError

/**
 * CartRepository.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:34
 * Android studio
 */

class CartRepository(
    private val cartService: CartApi,
    private val handler: CartApiHandler,
    private val cartLocalStorage: ICartLocalStorage,
    private val productsLocalStorage: IProductsLocalStorage,
    private val categoriesLocalStorage: ICategoriesLocalStorage
) : ICartRepository {
    override suspend fun updateLocalCart() {
        safeNetworkCall {
            withContext(Dispatchers.IO) {
                cartService.get()
            }
        }.onSuccess { response ->
            cartLocalStorage.clear()

            val cartEntities = response.result.map { cartProductDTO ->
                CartEntity(
                    amount = cartProductDTO.amount,
                    productID = cartProductDTO.product.id
                )
            }

            val products = response.result.map { it.product }
            val categories = products.map { it.category }

            categoriesLocalStorage.insert(categories)
            productsLocalStorage.insert(products)

            cartEntities.forEach { entity ->
                cartLocalStorage.insert(
                    entity = entity
                )
            }
        }
    }

    override suspend fun get(): Flow<ResourceState<BasketResponse>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            safeNetworkCall {
                withContext(Dispatchers.IO) {
                    cartService.get()
                }
            }.onFailure { error ->
                if (error.code == 401) {
                    cartLocalStorage.clear()
                    emit(ResourceState.Error(CartAppError.UnauthorizedError()))
                } else {
                    emit(ResourceState.Error(CartAppError.LoadCartError()))
                }
                return@onFailure
            }.onSuccess { response ->
                val cartEntities = response.result.map { cartProductDTO ->
                    CartEntity(
                        amount = cartProductDTO.amount,
                        productID = cartProductDTO.product.id
                    )
                }

                val products = response.result.map { it.product }
                val categories = products.map { it.category }

                categoriesLocalStorage.insert(categories)
                productsLocalStorage.insert(products)

                cartEntities.forEach { entity ->
                    cartLocalStorage.insert(
                        entity = entity
                    )
                }

                emit(ResourceState.Success(response))
                return@onSuccess
            }

            emit(ResourceState.Loading(isLoading = false))
        }

    override suspend fun getV2(): Flow<CheeseResult<CartError, BasketResponse>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get().onSuccess { response ->
            val cartEntities = response.result.map { cartProductDTO ->
                CartEntity(
                    amount = cartProductDTO.amount,
                    productID = cartProductDTO.product.id
                )
            }

            val products = response.result.map { it.product }
            val categories = products.map { it.category }

            categoriesLocalStorage.insert(categories)
            productsLocalStorage.insert(products)

            cartEntities.forEach { entity ->
                cartLocalStorage.insert(
                    entity = entity
                )
            }

            emit(CheeseResult.Success(response))
            return@onSuccess
        }.onError { error ->
            if (error == CartError.Unauthorized()) {
                cartLocalStorage.clear()
                emit(CheeseResult.Error(CartError.Unauthorized()))
            } else {
                emit(CheeseResult.Error(CartError.ReceiveError()))
            }
            return@onError
        }

        emit(CheeseResult.Loading(isLoading = false))
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
                    emit(ResourceState.Error(CartAppError.UnauthorizedError()))
                } else {
                    emit(ResourceState.Error(CartAppError.IncrementProductError()))
                }
                return@onFailure
            }.onSuccess { success ->
                if (success) {
                    cartLocalStorage.increment(currentAmount, productID)
                    emit(ResourceState.Success(success))
                } else {
                    emit(ResourceState.Error(CartAppError.IncrementProductError()))
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
                    withContext(Dispatchers.IO) {
                        cartService.update(
                            request = UpdateCartRequest(
                                productID = productID,
                                amount = currentAmount - 1
                            )
                        )
                    }
                }.onFailure { error ->
                    if (error.code == 401) {
                        cartLocalStorage.clear()
                        emit(ResourceState.Error(CartAppError.UnauthorizedError()))
                    } else {
                        emit(ResourceState.Error(CartAppError.DecrementProductError()))
                    }
                    return@onFailure
                }.onSuccess { success ->
                    if (success) {
                        cartLocalStorage.decrement(currentAmount, productID)
                        emit(ResourceState.Success(success))
                    } else {
                        emit(ResourceState.Error(CartAppError.DecrementProductError()))
                    }
                    return@onSuccess
                }
            } else {
                safeNetworkCall {
                    withContext(Dispatchers.IO) {
                        cartService.delete(productID = productID)
                    }
                }.onFailure { error ->
                    if (error.code == 401) {
                        cartLocalStorage.clear()
                        emit(ResourceState.Error(CartAppError.UnauthorizedError()))
                    } else {
                        emit(ResourceState.Error(CartAppError.DeleteProductError()))
                    }
                    return@onFailure
                }.onSuccess { success ->
                    if (success) {
                        cartLocalStorage.delete(productID)
                        emit(ResourceState.Success(success))
                    } else {
                        emit(ResourceState.Error(CartAppError.DeleteProductError()))
                    }
                    return@onSuccess
                }
            }

            emit(ResourceState.Loading(isLoading = false))
        }

    override suspend fun delete(productID: String): Flow<ResourceState<Boolean>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCall {
            withContext(Dispatchers.IO) {
                cartService.delete(productID = productID)
            }
        }.onFailure { error ->
            if (error.code == 401) {
                cartLocalStorage.clear()
                emit(ResourceState.Error(CartAppError.UnauthorizedError()))
            } else {
                emit(ResourceState.Error(CartAppError.DeleteProductError()))
            }
            return@onFailure
        }.onSuccess { success ->
            if (success) {
                cartLocalStorage.delete(productID)
                emit(ResourceState.Success(success))
            } else {
                emit(ResourceState.Error(CartAppError.DeleteProductError()))
            }
            return@onSuccess
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    override suspend fun clear(): Flow<ResourceState<Boolean>> =
        flow {
            emit(ResourceState.Loading(isLoading = true))

            safeNetworkCall {
                withContext(Dispatchers.IO) {
                    cartService.clear()
                }
            }.onFailure { error ->
                emit(ResourceState.Error(CartAppError.ClearError()))
                return@onFailure
            }.onSuccess { success ->
                if (success) {
                    cartLocalStorage.clear()
                    emit(ResourceState.Success(success))
                } else {
                    emit(ResourceState.Error(CartAppError.ClearError()))
                }
                return@onSuccess
            }

            emit(ResourceState.Loading(isLoading = true))
        }
}