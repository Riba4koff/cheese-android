package ru.antares.cheese_android.data.remote.services.addresses

import ru.antares.cheese_android.data.local.room.addresses.AddressEntity
import ru.antares.cheese_android.presentation.models.AddressModel

data class AddressDTO(
    val id: String,
    val userID: String,
    val city: String,
    val street: String,
    val house: String,
    val title: String,
    val apartment: String? = null,
    val building: String? = null,
    val entrance: String? = null,
    val floor: String? = null,
    val isDeleted: Boolean? = null
) {
    fun toModel() = AddressModel(
        id = id,
        userID = userID,
        city = city,
        street = street,
        house = house,
        title = title,
        apartment = apartment,
        building = building,
        entrance = entrance,
        floor = floor,
        isDeleted = isDeleted
    )

    fun toEntity() = AddressEntity(
        id = id,
        userID = userID,
        city = city,
        street = street,
        house = house,
        title = title,
        apartment = apartment,
        building = building,
        entrance = entrance,
        floor = floor,
        isDeleted = isDeleted
    )
}