package ru.antares.cheese_android.data.remote.services.main.catalog.models

import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

data class CategoryDTO(
    val id: String,
    val name: String,
    val position: Int,
    val parentID: String? = null
)

fun CategoryDTO.toCategoryUIModel() = CategoryUIModel(
    id = id, name = name, position = position, parentID = parentID
)

fun List<CategoryDTO>.toCategoryUIModels() = this.map { it.toCategoryUIModel() }