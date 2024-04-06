package ru.antares.cheese_android.presentation.view.main.community_graph.post

/**
 * PostEvent.kt
 * @author Павел
 * Created by on 06.04.2024 at 23:43
 * Android studio
 */

sealed interface PostEvent {
    data class AddProductToCart(
        val productID: String,
        val amount: Int
    ) : PostEvent

    data class RemoveProductFromCart(
        val productID: String,
        val amount: Int
    ) : PostEvent
}