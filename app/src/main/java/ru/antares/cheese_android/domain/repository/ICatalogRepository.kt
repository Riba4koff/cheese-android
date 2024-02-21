package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.dto.CategoryDTO
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.models.uiModels.CategoryUIModel

interface ICatalogRepository {
    suspend fun getListOfCategoryPairs(
        page: Int? = null,
        pageSize: Int? = null
    ): Flow<ResourceState<List<Pair<CategoryUIModel, List<CategoryUIModel>>>>>

    suspend fun getCategoriesByParentID(
        parentID: String,
        page: Int?,
        pageSize: Int?
    ): Flow<ResourceState<Pagination<CategoryUIModel>>>
}