package ru.antares.cheese_android.domain.paymentType

import com.google.gson.*
import java.lang.reflect.Type

class PaymentTypeAdapter : JsonSerializer<PaymentType>, JsonDeserializer<PaymentType> {
    override fun serialize(
        src: PaymentType?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", src?.javaClass?.simpleName)
        jsonObject.add("data", context?.serialize(src))
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PaymentType {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("type")?.asString
        val data = jsonObject?.get("data")

        return when (type) {
            "CASH" -> context?.deserialize(data, PaymentType.Cash::class.java) ?: PaymentType.Cash()
            "CARD_TO_COURIER" -> PaymentType.CardToCourier()
            "ONLINE" -> context?.deserialize(data, PaymentType.Online::class.java) ?: PaymentType.Online()
            else -> throw JsonParseException("Unknown type: $type")
        }
    }
}