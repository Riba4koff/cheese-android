package ru.antares.cheese_android.data.remote.dto

import ru.antares.cheese_android.data.local.room.dao.products.ProductEntity
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
)

fun ProductDTO.toEntity() = ProductEntity(
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

fun ProductDTO.toProductModel() = ProductModel(
    id = id,
    name = name,
    price = price,
    description = description,
    unit = unit,
    unitName = unitName,
    categoryId = categoryId,
    recommend = recommend,
    outOfStock = outOfStock,
    category = category.toCategoryModel(),
    categories = categories.toCategoryModels()
)

fun List<ProductDTO>.toProductModels() = this.map { dto -> dto.toProductModel() }