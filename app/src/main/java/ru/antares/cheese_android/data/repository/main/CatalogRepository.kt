package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.dao.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.remote.dto.CategoryDTO
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogUIError
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryUIError

/**
 * @author Pavel Rybakov
 * */

class CatalogRepository(
    private val service: CatalogService,
    private val categoresLocalStorage: ICategoriesLocalStorage
) : ICatalogRepository {

    /**
     * Returns a list of pairs with the value <parent category, List<child category>>
     * */
    override suspend fun getListOfCategoryPairs(
        page: Int?,
        pageSize: Int?
    ): Flow<ResourceState<List<Pair<CategoryModel, List<CategoryModel>>>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        val parentCategories = parentCategories(
            page = page,
            pageSize = pageSize
        ).getOrNull()

        val pairsOfCategory = parentCategories?.result?.map { categoryDTO ->
            val childCategories =
                childCategories(categoryDTO.id)
                    .getOrNull()
                    ?.result
                    ?.map { it.toModel() }
                    ?: emptyList()

            if (childCategories.isNotEmpty()) {
                categoryDTO.toModel() to childCategories
            } else {
                null
            }
        }

        if (pairsOfCategory == null) {
            emit(
                ResourceState.Error(
                    error = CatalogUIError.Loading()
                )
            )
        } else {
            emit(ResourceState.Success(data = pairsOfCategory.filterNotNull()))
        }
    }

    override suspend fun getCategoriesByParentID(
        parentID: String,
        page: Int?,
        pageSize: Int?
    ): Flow<ResourceState<Pagination<CategoryModel>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCallWithPagination {
            service.get(parentID = parentID, hasParent = true, page = page, pageSize = pageSize)
        }.onSuccess { data: Pagination<CategoryDTO> ->
            val categories = data.result

            categoresLocalStorage.insert(categories)

            emit(ResourceState.Success(data = data.map { it.toModel() }))
        }.onFailure { error ->
            emit(ResourceState.Error(CatalogParentCategoryUIError.Loading()))
        }

        emit(ResourceState.Loading(isLoading = false))
    }

    private suspend fun parentCategories(
        page: Int?, pageSize: Int?
    ): NetworkResponse<Pagination<CategoryDTO>> {
        return safeNetworkCallWithPagination {
            service.get(hasParent = false, page = page, pageSize = pageSize)
        }.onFailure { error ->
            Log.d("LOAD_PARENT_CATEGORIES", error.message)
        }.onSuccess { pagination ->
            val categories = pagination.result

            categoresLocalStorage.insert(categories)
        }
    }

    private suspend fun childCategories(parentID: String): NetworkResponse<Pagination<CategoryDTO>> {
        return safeNetworkCallWithPagination {
            service.get(
                parentID = parentID,
                hasParent = true,
                pageSize = 6
            )
        }.onFailure { error ->
            Log.d("LOAD_PARENT_CATEGORIES", error.message)
        }.onSuccess { pagination ->
            val categories = pagination.result

            categoresLocalStorage.insert(categories)
        }
    }
}