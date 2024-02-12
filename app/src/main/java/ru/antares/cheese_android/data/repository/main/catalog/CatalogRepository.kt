package ru.antares.cheese_android.data.repository.main.catalog

import android.util.Log
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.remote.services.main.catalog.models.CategoryDTO
import ru.antares.cheese_android.data.remote.services.main.catalog.models.toCategoryUIModel
import ru.antares.cheese_android.data.remote.services.main.catalog.models.toCategoryUIModels
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository

class CatalogRepository(
    private val service: CatalogService
) : ICatalogRepository {
    private companion object {
        const val CANNOT_LOAD_CATEGORIES = "Не удалось загрузить категории"
    }

    override suspend fun get(): NetworkResponse<List<Pair<CategoryUIModel, List<CategoryUIModel>>>> {
        val parentCategories = loadParentCategories().getOrNull()
        val pairsOfCategory = parentCategories?.result?.map { categoryDTO ->
            val childCategories =
                loadChildCategories(categoryDTO.id).getOrNull()?.result?.toCategoryUIModels()
                    ?: emptyList()

            if (childCategories.isNotEmpty()) categoryDTO.toCategoryUIModel() to childCategories
            else null
        }
        return if (pairsOfCategory == null) NetworkResponse.Error(CANNOT_LOAD_CATEGORIES)
        else NetworkResponse.Success(pairsOfCategory.filterNotNull())
    }

    private suspend fun loadParentCategories(): NetworkResponse<Pagination<CategoryDTO>> {
        return safeNetworkCallWithPagination {
            service.get(hasParent = false)
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