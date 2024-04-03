package ru.antares.cheese_android.data.remote.services.community.dto

import ru.antares.cheese_android.data.remote.dto.ProductDTO
import ru.antares.cheese_android.domain.models.community.PostModel

data class PostDTO(
    val id: String,
    val title: String,
    val subtitle: String,
    val createdAt: String,
    val publishedAt: String,
    val description: String,
    val activityModel: ActivityDTO? = null,
    val products: List<ProductDTO>,
    val posts: List<InnerPostDTO>? = null
) {
    fun toModel() = PostModel(
        id = id,
        title = title,
        subtitle = subtitle,
        createdAt = createdAt,
        publishedAt = publishedAt,
        description = description,
        activityModel = activityModel?.toModel(),
        products = products.map { dto -> dto.toModel() },
        posts = posts?.map { it.toModel() }
    )
}