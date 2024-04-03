package ru.antares.cheese_android.data.remote.services.community.dto

import ru.antares.cheese_android.data.remote.dto.CategoryDTO
import ru.antares.cheese_android.data.remote.dto.ProductDTO
import ru.antares.cheese_android.domain.models.community.InnerPostModel

data class InnerPostDTO(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val createdAt: String,
    val categories: List<CategoryDTO>,
    val category: CategoryDTO,
    val products: List<ProductDTO>,
) {
    fun toModel() = InnerPostModel(
        id = id,
        title = title,
        subtitle = subtitle,
        description = description,
        createdAt = createdAt,
        categories = categories.map { it.toModel() },
        category = category.toModel(),
        products = products.map { it.toModel() }
    )
}