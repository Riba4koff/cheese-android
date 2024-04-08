package ru.antares.cheese_android.data.remote.services.main.products

import ru.antares.cheese_android.data.local.room.products.ProductEntity
import ru.antares.cheese_android.data.remote.services.main.catalog.CategoryDTO
import ru.antares.cheese_android.domain.models.ProductModel

data class ProductDTO(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val unit: Int,
    val category: CategoryDTO,
    val categoryId: String,
    val categories: List<CategoryDTO>,
    val recommend: Boolean,
    val outOfStock: Boolean,
    val unitName: String
) {
    fun toModel() = ProductModel(
        id = id,
        name = name,
        price = price,
        description = description,
        unit = unit,
        unitName = unitName,
        categoryId = categoryId,
        recommend = recommend,
        outOfStock = outOfStock,
        category = category.toModel(),
        categories = categories.map { it.toModel() }
    )

    fun toEntity() = ProductEntity(
        id = id,
        name = name,
        price = price,
        description = description,
        unit = unit,
        unitName = unitName,
        categoryId = categoryId,
        recommend = recommend,
        outOfStock = outOfStock,
    )
}
