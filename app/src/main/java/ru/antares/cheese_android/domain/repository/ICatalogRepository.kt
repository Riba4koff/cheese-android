package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.models.CategoryDTO
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewState

interface ICatalogRepository {
    suspend fun getListOfCategoryPairs(
        page: Int? = null,
        pageSize: Int? = null
    ): Flow<CatalogViewState>

    suspend fun getCategoriesByParentID(
        parentID: String,
        page: Int?,
        pageSize: Int?
    ): NetworkResponse<Pagination<CategoryDTO>>
}