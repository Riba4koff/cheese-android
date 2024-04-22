package ru.antares.cheese_android.data.remote.api.cart

/**
 * UpdateCartRequest.kt
 * @author Павел
 * Created by on 23.02.2024 at 14:51
 * Android studio
 */

data class UpdateCartRequest(
    val productID: String,
    val amount: Int
)
