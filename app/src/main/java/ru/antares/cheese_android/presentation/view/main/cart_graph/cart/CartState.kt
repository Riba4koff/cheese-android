package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.ProductModel
import javax.annotation.concurrent.Immutable

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:35
 * Android Studio
 */

@optics
@Immutable
data class CartState(
    val loading: Boolean = true,
    val authorized: Boolean? = null,
    val cartLoading: Boolean = false,
    val error: UIError? = null,
    val products: List<CartProductModel> = emptyList(),
    val totalCost: Double = 0.0,
    val totalCostWithDiscount: Double = 0.0,
    val currentPage: Int = 0
) { companion object }