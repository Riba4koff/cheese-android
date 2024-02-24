package ru.antares.cheese_android.presentation.models

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.models.ProductModel

@optics
@Immutable
data class ProductUIModel(
    val value: ProductModel,
    val countInCart: Int,
    val isFavorite: Boolean = false
) { companion object }
