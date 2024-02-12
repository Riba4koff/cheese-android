package ru.antares.cheese_android.domain.repository

import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

interface ICatalogRepository {
    suspend fun get(): NetworkResponse<List<Pair<CategoryUIModel, List<CategoryUIModel>>>>
}