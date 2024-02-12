package ru.antares.cheese_android.domain.usecases.catalog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

class GetCategoriesUseCase(
    /* TODO: database */
) {
    private val _value: MutableStateFlow<List<Pair<CategoryUIModel, List<CategoryUIModel>>>> =
        MutableStateFlow(listOf())
    val value: Flow<List<Pair<CategoryUIModel, List<CategoryUIModel>>>> = _value


    suspend fun emit(value: List<Pair<CategoryUIModel, List<CategoryUIModel>>>) {
        _value.emit(value)
    }
}