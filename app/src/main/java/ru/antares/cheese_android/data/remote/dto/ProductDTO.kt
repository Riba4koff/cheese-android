package ru.antares.cheese_android.data.remote.dto

import ru.antares.cheese_android.domain.models.uiModels.ProductUIModel

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

fun ProductDTO.toProductUIModel() = ProductUIModel(
    id = id,
    name = name,
    price = price,
    description = description,
    unit = unit,
    unitName = unitName,
    categoryId = categoryId,
    recommend = recommend,
    outOfStock = outOfStock,
    category = category.toCategoryUIModel(),
    categories = categories.toCategoryUIModels()
)

fun List<ProductDTO>.toProductUIModels() = this.map { dto -> dto.toProductUIModel() }