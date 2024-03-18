package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

sealed interface ProfileEvent {
    data object Logout: ProfileEvent
    data object DeleteAccount: ProfileEvent
    data object LoadProfile: ProfileEvent
}