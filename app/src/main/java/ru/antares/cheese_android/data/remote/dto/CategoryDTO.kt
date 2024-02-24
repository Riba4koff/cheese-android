package ru.antares.cheese_android.data.remote.dto

import ru.antares.cheese_android.data.local.room.dao.catalog.CategoryEntity
import ru.antares.cheese_android.domain.models.CategoryModel

data class CategoryDTO(
    val id: String,
    val name: String,
    val position: Int,
    val parentID: String? = null
)

fun CategoryDTO.toEntity() = CategoryEntity(
    id, name, position, parentID
)

fun CategoryDTO.toCategoryModel() = CategoryModel(
    id = id,
    name = name,
    position = position,
    parentID = parentID
)

fun List<CategoryDTO>.toCategoryModels() = this.map { it.toCategoryModel() }