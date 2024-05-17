package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 18:06
 * Android Studio
 */

sealed interface CreateAddressNavigationEvent {
    data object NavigateBack: CreateAddressNavigationEvent
}