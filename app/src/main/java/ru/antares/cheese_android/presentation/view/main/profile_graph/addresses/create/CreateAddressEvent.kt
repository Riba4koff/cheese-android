package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 18:03
 * Android Studio
 */

sealed interface CreateAddressEvent {
    data class OnCityChange(val value: String) : CreateAddressEvent
    data class OnStreetChange(val value: String) : CreateAddressEvent
    data class OnHouseChange(val value: String) : CreateAddressEvent
    data class OnBuildingChange(val value: String) : CreateAddressEvent
    data class OnEntranceChange(val value: String) : CreateAddressEvent
    data class OnFloorChange(val value: String) : CreateAddressEvent
    data class OnApartmentChange(val value: String) : CreateAddressEvent
    data class OnCommentChange(val value: String) : CreateAddressEvent
    data class OnSaveClick(
        val city: String,
        val street: String,
        val house: String,
        val building: String,
        val entrance: String,
        val apartment: String,
        val floor: String,
        val comment: String
    ) : CreateAddressEvent
}