package ru.antares.cheese_android.domain.models.community

import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:27
 * Android Studio
 */

data class PostModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val createdAt: String,
    val publishedAt: String,
    val description: String,
    val activityModel: ActivityModel?,
    val products: List<ProductModel>,
    val posts: List<InnerPostModel>?
)
