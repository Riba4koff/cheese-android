package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.remote.dto.ProductDTO
import ru.antares.cheese_android.data.remote.dto.toProductUIModel
import ru.antares.cheese_android.data.remote.dto.toProductUIModels
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.products.ProductsService
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.uiModels.ProductUIModel
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailUIError

/**
 * @author Pavel Rybakov
 * */
class ProductsRepository(
    private val productsService: ProductsService
) : IProductsRepository {
    companion object {
        const val GET_PRODUCTS_ERROR_TAG = "GET_PRODUCTS_ERROR"
        const val GET_PRODUCT_ERROR_TAG = "GET_PRODUCT_ERROR"
    }

    /**
     * GET: List of products
     * */
    override suspend fun get(
        categoryID: String,
        page: Int?,
        size: Int?,
        sortByColumn: String?
    ): Flow<ResourceState<Pagination<ProductUIModel>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCallWithPagination {
            productsService.getByCategoryID(
                categoryID = categoryID,
                page = page,
                size = size,
                sortByColumn = sortByColumn
            )
        }.onSuccess { pagination ->
            val mappedList = pagination.result.toProductUIModels()
            emit(
                ResourceState.Success(
                    Pagination(
                        result = mappedList,
                        sizeResult = pagination.sizeResult,
                        page = pagination.page,
                        amountOfPages = pagination.amountOfPages,
                        amountOfAll = pagination.amountOfAll
                    )
                )
            )
            return@onSuccess
        }.onFailure { error ->
            Log.d(GET_PRODUCTS_ERROR_TAG, error.toString())
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    /**
     * GET: Product by id
     * */
    override suspend fun get(id: String): Flow<ResourceState<ProductUIModel>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCall { productsService.getProductByID(productID = id) }.onSuccess { productDTO ->
            val productUIModel = productDTO.toProductUIModel()
            emit(ResourceState.Success(productUIModel))
            return@onSuccess
        }.onFailure { error ->
            Log.d(GET_PRODUCT_ERROR_TAG, error.toString())
            emit(ResourceState.Error(ProductDetailUIError.LoadingError()))
        }
    }
}