package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

sealed interface PersonalDataNavigationEvent {
    data object PopBackStack: PersonalDataNavigationEvent
}