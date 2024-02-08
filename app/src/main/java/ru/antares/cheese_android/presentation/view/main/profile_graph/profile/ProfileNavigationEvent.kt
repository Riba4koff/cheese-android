package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

sealed interface ProfileNavigationEvent {
    data object NavigateToPersonalData: ProfileNavigationEvent
    data object NavigateToSavedAddresses: ProfileNavigationEvent
    data object NavigateToOrders: ProfileNavigationEvent
    data object NavigateToAboutApp: ProfileNavigationEvent
    data object NavigateToMyTickets: ProfileNavigationEvent
    data object Logout: ProfileNavigationEvent
    data object Authorize: ProfileNavigationEvent
}