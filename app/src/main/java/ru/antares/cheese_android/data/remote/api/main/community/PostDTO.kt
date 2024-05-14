package ru.antares.cheese_android.data.remote.api.main.community

import ru.antares.cheese_android.data.remote.api.main.products.ProductDTO

data class PostDTO(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val createdAt: String,
    val publishedAt: String,
    val posts: List<PostDTO>,
    val products: List<ProductDTO>
)