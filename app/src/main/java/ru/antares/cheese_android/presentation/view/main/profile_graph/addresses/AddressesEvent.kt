package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 16:03
 * Android Studio
 */

sealed interface AddressesEvent {
    data class LoadNextPage(val page: Int, val size: Int): AddressesEvent
    data class RemoveAddress(val id: String): AddressesEvent
}