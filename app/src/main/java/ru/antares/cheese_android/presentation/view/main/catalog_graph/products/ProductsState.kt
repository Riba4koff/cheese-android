package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.errors.ProductModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * ProductState.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:10
 * Android studio
 */

@optics
@Immutable
data class ProductsState(
    val loading: Boolean = true,
    val products: List<ProductUIModel> = emptyList(),
    val error: UIError? = null,
    val loadingNextPage: Boolean = true,
    val endReached: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 4
) {
    companion object
}
