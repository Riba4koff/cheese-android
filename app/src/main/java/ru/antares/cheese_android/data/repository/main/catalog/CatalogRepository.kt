package ru.antares.cheese_android.data.repository.main.catalog

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.remote.services.main.catalog.models.CategoryDTO
import ru.antares.cheese_android.data.remote.services.main.catalog.models.toCategoryUIModel
import ru.antares.cheese_android.data.remote.services.main.catalog.models.toCategoryUIModels
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogUIError

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


class CatalogRepository(
    private val service: CatalogService
) : ICatalogRepository {
    private companion object {
        const val CANNOT_LOAD_CATEGORIES = "Не удалось загрузить категории"
    }

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
                    error = CatalogUIError.Loading(message = CANNOT_LOAD_CATEGORIES)
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
    ): NetworkResponse<Pagination<CategoryDTO>> = safeNetworkCallWithPagination {
        service.get(parentID = parentID, hasParent = true, page = page, pageSize = pageSize)
    }.onFailure { error -> Log.d("LOAD_CATEGORIES_BY_PARENT_ID", error.message) }

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