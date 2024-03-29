package ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm

/**
 * @author pavelrybakov
 * Created 29.03.2024 at 15:57
 * Android Studio
 */

sealed interface ConfirmOrderNavigationEvent {
    data object NavigateBack: ConfirmOrderNavigationEvent
    data object NavigateToPay: ConfirmOrderNavigationEvent
}