package ru.antares.cheese_android.data.remote.services.community

import ru.antares.cheese_android.data.remote.services.main.products.ProductDTO

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