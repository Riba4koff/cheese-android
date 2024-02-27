package ru.antares.cheese_android.domain.models

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:40
 * Android Studio
 */

data class CartProductModel(
    val amount: Int,
    val price: Double,
    val priceWithDiscount: Int? = null,
    val product: ProductModel
)