package ru.antares.cheese_android.domain.errors

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

@optics
@Immutable
data class ProductModel(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val unit: Int,
    val category: CategoryUIModel,
    val categoryId: String,
    val categories: List<CategoryUIModel>,
    val recommend: Boolean,
    val outOfStock: Boolean,
    val unitName: String
) {
    companion object

    val imageUrl = "https://mobile-backend.cheese.asg-demo.ru/api/v1/store/products/products/$id/image"
}

fun ProductModel.toUIModel(countInCart: Int = 0, isFavorite: Boolean = false) = ProductUIModel(
    value = this,
    countInCart = countInCart,
    isFavorite = isFavorite
)

fun List<ProductModel>.toUIModels() = this.map { it.toUIModel() }