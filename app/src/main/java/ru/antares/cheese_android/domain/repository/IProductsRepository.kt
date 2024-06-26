package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author Pavel Rybakov
 * */
interface IProductsRepository{
    suspend fun get(
        categoryID: String,
        page: Int? = null,
        size: Int? = null,
        sortByColumn: String? = null
    ): Flow<ResourceState<Pagination<ProductModel>>>
    suspend fun get(id: String): Flow<ResourceState<ProductModel>>
    suspend fun get(
        name: String? = null,
        recommend: Boolean,
        page: Int? = null,
        size: Int? = null,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): Flow<ResourceState<Pagination<ProductModel>>>
}