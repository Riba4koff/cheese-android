package ru.antares.cheese_android.domain.paymentType

import com.google.gson.annotations.SerializedName
import ru.antares.cheese_android.R

sealed class PaymentType(
    @SerializedName("name")
    val name: String,
    val value: String,
    val icon: Int
) {
    enum class Type(val value: String) {
        CASH("Наличные"),
        CARD_TO_COURIER("Картой курьеру"),
        ONLINE("Онлайн")
    }

    class Cash(
        val cash: Double? = null
    ) : PaymentType(
        name = Type.CASH.name,
        value = Type.CASH.value,
        icon = R.drawable.ic_cash
    )

    class CardToCourier(
    ) : PaymentType(
        name = Type.CARD_TO_COURIER.name,
        value = Type.CARD_TO_COURIER.value,
        icon = R.drawable.ic_credit_card
    )

    class Online(
        var paymentStatus: String? = null,
    ) : PaymentType(
        name = Type.ONLINE.name,
        value = Type.ONLINE.value,
        icon = R.drawable.ic_credit_card
    )
}
