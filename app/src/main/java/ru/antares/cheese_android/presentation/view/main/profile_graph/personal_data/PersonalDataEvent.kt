package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

sealed interface PersonalDataEvent {

    data class OnSurnameChange(val surname: String) : PersonalDataEvent
    data class OnNameChange(val name: String) : PersonalDataEvent
    data class OnPatronymicChange(val patronymic: String) : PersonalDataEvent
    data class OnBirthdayChange(val birthday: String) : PersonalDataEvent
    data class OnPhoneChange(val phone: String) : PersonalDataEvent
    data class OnEmailChange(val email: String) : PersonalDataEvent
    data object Confirm : PersonalDataEvent
}