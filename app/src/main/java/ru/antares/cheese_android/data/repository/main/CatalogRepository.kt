package ru.antares.cheese_android.data.repository.main.catalog

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.remote.dto.CategoryDTO
import ru.antares.cheese_android.data.remote.dto.toCategoryUIModel
import ru.antares.cheese_android.data.remote.dto.toCategoryUIModels
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogUIError
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryUIError

/*
* Получить категории
*
* Если запрос на получение родительских категорий выполнен неудачно ->
* -> Вернуть ошибку о том, что не удалось загрузить категории
*
* Если запрос на получение родительских категорий выполнен успешно ->
* -> Записать данные в базу -> Получить дочерние категории родительских категорий ->
* -> смапить их к типу List<Pair<CategoryUIModel, List<CategoryUIModel>>>, где левый тип - это
*  родительская категория, а правый тип - дочерние категории -> записать в базу дочерние категории ->
* вернуть список полученных данных
* */

/**
 * @author Pavel Rybakov
 * */

class CatalogRepository(
    private val service: CatalogService
) : ICatalogRepository {

    /**
     * Returns a list of pairs with the value <parent category, List<child category>>
     * */
    override suspend fun getListOfCategoryPairs(
        page: Int?,
        pageSize: Int?
    ): Flow<ResourceState<List<Pair<CategoryUIModel, List<CategoryUIModel>>>>> = flow {
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
                    ?.toCategoryUIModels()
                    ?: emptyList()

            if (childCategories.isNotEmpty()) {
                categoryDTO.toCategoryUIModel() to childCategories
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
    ): Flow<ResourceState<Pagination<CategoryUIModel>>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        safeNetworkCallWithPagination {
            service.get(parentID = parentID, hasParent = true, page = page, pageSize = pageSize)
        }.onSuccess { pagination ->
            emit(
                ResourceState.Success(
                    data = Pagination(
                        result = pagination.result.toCategoryUIModels(),
                        sizeResult = pagination.sizeResult,
                        page = pagination.page,
                        amountOfAll = pagination.amountOfAll,
                        amountOfPages = pagination.amountOfPages
                    )
                )
            )
        }.onFailure { error ->
            Log.d("LOAD_CHILD_CATEGOIRES_ERROR", error.message)
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
        }
    }
}