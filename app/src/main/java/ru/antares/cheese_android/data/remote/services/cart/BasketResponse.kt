package ru.antares.cheese_android.data.remote.services.cart

data class BasketResponse(
    val sizeResult: Int,
    val result: List<CartProductDTO>,
    val page: Int,
    val amountOfAll: Int,
    val amountOfPages: Int,
    val totalCost: Int,
    val totalCostWithDiscount: Int
)