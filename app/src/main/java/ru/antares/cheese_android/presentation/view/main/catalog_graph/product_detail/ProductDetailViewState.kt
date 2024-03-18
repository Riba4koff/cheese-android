package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * ProductDetailViewState.kt
 * @author Павел
 * Created by on 22.02.2024 at 22:47
 * Android studio
 */

@optics
@Immutable
data class ProductDetailViewState(
    val loading: Boolean = true,
    val product: ProductUIModel? = null,
    val appError: AppError? = null,
    val loadingNextPageRecommendations: Boolean = true,
    val recommendations: List<ProductUIModel> = emptyList(),
    val endReached: Boolean = false,
    val pageSize: Int = 4,
    val currentPage: Int = 0,
    val loadingCart: Boolean = false
) { companion object }
