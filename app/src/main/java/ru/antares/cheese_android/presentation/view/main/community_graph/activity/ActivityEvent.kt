package ru.antares.cheese_android.presentation.view.main.community_graph.activity

/**
 * CommunityDetailEvent.kt
 * @author Павел
 * Created by on 06.04.2024 at 19:55
 * Android studio
 */

sealed interface ActivityEvent {
    data class BuyTicket(val activityID: String) : ActivityEvent
    data class AddProductToCart(
        val productID: String,
        val amount: Int
    ) : ActivityEvent

    data class RemoveProductFromCart(
        val productID: String,
        val amount: Int
    ) : ActivityEvent
}