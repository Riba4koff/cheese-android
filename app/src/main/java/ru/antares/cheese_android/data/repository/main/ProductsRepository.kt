package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.products.IProductsLocalStorage
import ru.antares.cheese_android.data.remote.api.main.products.ProductDTO
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.data.remote.api.main.products.ProductsApi
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsAppError

/**
 * @author Pavel Rybakov
 * */
class ProductsRepository(
    private val productsService: ProductsApi,
    private val productsLocalStorage: IProductsLocalStorage,
    private val categoriesLocalStorage: ICategoriesLocalStorage
) : IProductsRepository {
    companion object {
        const val GET_PRODUCTS_ERROR_TAG = "GET_PRODUCTS_ERROR"
        const val GET_PRODUCT_ERROR_TAG = "GET_PRODUCT_ERROR"
    }

    /**
     * GET
     * List of products
     *
     * @param categoryID id of category
     * @param page number of received page
     * @param size size of received page
     * @param sortByColumn type of sorting - ASC/DESC
     *
     * @return [Flow]<[ResourceState]<[Pagination]<[ProductModel]>>>
     * */
    override suspend fun get(
        categoryID: String,
        page: Int?,
        size: Int?,
        sortByColumn: String?
    ): Flow<ResourceState<Pagination<ProductModel>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCallWithPagination {
            productsService.getByCategoryID(
                categoryID = categoryID,
                page = page,
                size = size,
                sortByColumn = sortByColumn
            )
        }.onSuccess { data: Pagination<ProductDTO> ->
            val products = data.result
            val categories = data.result.map { it.category }

            categoriesLocalStorage.insert(categories)
            productsLocalStorage.insert(products)

            emit(
                ResourceState.Success(data.map { it.toModel() })
            )

            return@onSuccess
        }.onFailure { error ->
            Log.d(GET_PRODUCTS_ERROR_TAG, error.toString())
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    /**
     * GET
     * Product by id
     *
     * @param id id of category
     *
     * @return  [Flow]<[ResourceState]<[ProductModel]>>
     * */
    override suspend fun get(id: String): Flow<ResourceState<ProductModel>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        delay(300)

        safeNetworkCall { productsService.getProductByID(productID = id) }.onSuccess { productDTO ->
            val productUIModel = productDTO.toModel()
            emit(ResourceState.Success(productUIModel))
            return@onSuccess
        }.onFailure { error ->
            Log.d(GET_PRODUCT_ERROR_TAG, error.toString())
            emit(ResourceState.Error(ProductsAppError.LoadingError()))
            return@onFailure
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    override suspend fun get(
        name: String?,
        recommend: Boolean,
        page: Int?,
        size: Int?,
        sortDirection: String?,
        sortByColumn: String?
    ): Flow<ResourceState<Pagination<ProductModel>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCallWithPagination {
            productsService.get(name, recommend, page, size, sortDirection, sortByColumn)
        }.onFailure { error ->
            Log.d(GET_PRODUCTS_ERROR_TAG, error.toString())
            emit(ResourceState.Error(ProductsAppError.LoadingError()))
            return@onFailure
        }.onSuccess { data: Pagination<ProductDTO> ->
            val mapped = data.map { it.toModel() }
            emit(ResourceState.Success(mapped))
        }

        emit(ResourceState.Loading(isLoading = false))
    }
}