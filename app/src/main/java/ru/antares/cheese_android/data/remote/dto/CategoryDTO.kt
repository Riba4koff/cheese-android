package ru.antares.cheese_android.data.remote.dto

import ru.antares.cheese_android.presentation.models.CategoryUIModel

data class CategoryDTO(
    val id: String,
    val name: String,
    val position: Int,
    val parentID: String? = null
)

fun CategoryDTO.toCategoryUIModel() = CategoryUIModel(
    id = id,
    name = name,
    position = position,
    parentID = parentID
)

fun List<CategoryDTO>.toCategoryUIModels() = this.map { it.toCategoryUIModel() }