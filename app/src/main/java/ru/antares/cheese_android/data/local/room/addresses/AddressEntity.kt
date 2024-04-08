package ru.antares.cheese_android.data.local.room.addresses

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.antares.cheese_android.presentation.models.AddressModel

@Entity("addresses")
data class AddressEntity(
    @PrimaryKey
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
    fun toModel(): AddressModel {
        return AddressModel(
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
}