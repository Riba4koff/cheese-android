package ru.antares.cheese_android.data.remote.api.cart

import ru.antares.cheese_android.data.remote.api.main.products.ProductDTO
import ru.antares.cheese_android.domain.models.CartProductModel

data class CartProductDTO(
    val amount: Int,
    val price: Double,
    val priceWithDiscount: Int,
    val product: ProductDTO
)

fun CartProductDTO.toModel() = CartProductModel(
    amount = amount,
    price = price,
    priceWithDiscount = priceWithDiscount,
    product = product.toModel()
)