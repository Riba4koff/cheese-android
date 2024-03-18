package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.dao.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.dao.products.IProductsLocalStorage
import ru.antares.cheese_android.data.remote.dto.toProductModel
import ru.antares.cheese_android.data.remote.dto.toProductModels
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.products.ProductsService
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailAppError

/**
 * @author Pavel Rybakov
 * */
class ProductsRepository(
    private val productsService: ProductsService,
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
        }.onSuccess { pagination ->
            val products = pagination.result
            val categories = pagination.result.map { it.category }

            productsLocalStorage.insert(products)
            categoriesLocalStorage.insert(categories)

            emit(
                ResourceState.Success(
                    Pagination(
                        result = pagination.result.toProductModels(),
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
            val productUIModel = productDTO.toProductModel()
            emit(ResourceState.Success(productUIModel))
            return@onSuccess
        }.onFailure { error ->
            Log.d(GET_PRODUCT_ERROR_TAG, error.toString())
            emit(ResourceState.Error(ProductDetailAppError.LoadingError()))
            return@onFailure
        }

        emit(ResourceState.Loading(isLoading = false))
    }
}