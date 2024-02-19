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
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogUIError
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewState

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
    ): Flow<CatalogViewState> = flow {
        emit(CatalogViewState.Loading())

        val parentCategories = loadParentCategories(page = page, pageSize = pageSize).getOrNull()

        val pairsOfCategory = parentCategories?.result?.map { categoryDTO ->
            val childCategories =
                loadChildCategories(categoryDTO.id).getOrNull()?.result?.toCategoryUIModels()
                    ?: emptyList()

            if (childCategories.isNotEmpty()) categoryDTO.toCategoryUIModel() to childCategories
            else null
        }

        if (pairsOfCategory == null) emit(
            CatalogViewState.Error(
                error = CatalogUIError.Loading(
                    message = CANNOT_LOAD_CATEGORIES
                )
            )
        )
        else emit(CatalogViewState.Success(listOfCategoryPairs = pairsOfCategory.filterNotNull()))
    }

    override suspend fun getCategoriesByParentID(
        parentID: String,
        page: Int?,
        pageSize: Int?
    ): NetworkResponse<Pagination<CategoryDTO>> = safeNetworkCallWithPagination {
        service.get(parentID = parentID, hasParent = true, page = page, pageSize = pageSize)
    }.onFailure { message -> Log.d("LOAD_CATEGORIES_BY_PARENT_ID", message) }

    private suspend fun loadParentCategories(
        page: Int?, pageSize: Int?
    ): NetworkResponse<Pagination<CategoryDTO>> {
        return safeNetworkCallWithPagination {
            service.get(hasParent = false, page = page, pageSize = pageSize)
        }.onFailure { message ->
            Log.d("LOAD_PARENT_CATEGORIES", message)
        }
    }

    private suspend fun loadChildCategories(parentID: String): NetworkResponse<Pagination<CategoryDTO>> {
        return safeNetworkCallWithPagination {
            service.get(
                parentID = parentID,
                hasParent = true,
                pageSize = 6
            )
        }.onFailure { message ->
            Log.d("LOAD_PARENT_CATEGORIES", message)
        }
    }
}