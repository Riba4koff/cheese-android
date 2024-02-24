package ru.antares.cheese_android.data.remote.services.cart

import ru.antares.cheese_android.data.remote.dto.ProductDTO

data class CartProductDTO(
    val amount: Int,
    val price: Int,
    val priceWithDiscount: Int,
    val product: ProductDTO
)