package ru.antares.cheese_android.data.remote.api.community.dto

import ru.antares.cheese_android.data.remote.api.main.products.ProductDTO
import ru.antares.cheese_android.domain.models.community.InnerPostModel

data class InnerPostDTO(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val createdAt: String,
    val products: List<ProductDTO>,
) {
    fun toModel() = InnerPostModel(
        id = id,
        title = title,
        subtitle = subtitle,
        description = description,
        createdAt = createdAt,
        products = products.map { it.toModel() }
    )
}