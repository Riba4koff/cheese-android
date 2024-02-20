package ru.antares.cheese_android.domain.models.uiModels

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@optics
@Immutable
data class ProductUIModel(
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
) { companion object }
