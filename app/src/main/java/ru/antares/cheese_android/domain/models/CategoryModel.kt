package ru.antares.cheese_android.domain.models

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.data.local.room.catalog.CategoryEntity

@Immutable
data class CategoryModel(
    val id: String = "",
    val name: String = "",
    val position: Int = 0,
    val parentID: String? = null
) {
    val imageLink: String
        get() = "https://mobile-backend.cheese.asg-demo.ru/api/v1/store/categories/${id}/image"
}

fun CategoryModel.toEntity() = CategoryEntity(
    id = id,
    name = name,
    position = position,
    parentID = parentID
)