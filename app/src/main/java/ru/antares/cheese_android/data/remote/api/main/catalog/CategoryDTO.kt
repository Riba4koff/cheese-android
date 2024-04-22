package ru.antares.cheese_android.data.remote.api.main.catalog

import ru.antares.cheese_android.data.local.room.catalog.CategoryEntity
import ru.antares.cheese_android.domain.models.CategoryModel

data class CategoryDTO(
    val id: String,
    val name: String,
    val position: Int,
    val parentID: String? = null
) {
    fun toModel() = CategoryModel(
        id = id,
        name = name,
        position = position,
        parentID = parentID
    )
    fun toEntity() = CategoryEntity(
        id, name, position, parentID
    )
}
