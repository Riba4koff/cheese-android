package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 16:04
 * Android Studio
 */

sealed interface AddressesNavigationEvent {
    data object NavigateBack: AddressesNavigationEvent
    data object NavigateToAddAddress: AddressesNavigationEvent
}