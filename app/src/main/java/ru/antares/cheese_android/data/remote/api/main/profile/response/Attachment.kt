package ru.antares.cheese_android.data.remote.api.main.profile.response

import com.google.gson.annotations.SerializedName

sealed class Attachment {
    abstract val value: String
    abstract val verified: Boolean
    abstract val typeName: String

    data class Email(
        @SerializedName("email") val email: String,
        override val verified: Boolean = false
    ) : Attachment() {
        override val value: String = email
        override val typeName: String = TYPE_NAME

        companion object {
            const val TYPE_NAME = "EMAIL"
        }
    }

    data class Phone(
        @SerializedName("phone") val phone: String,
        override val verified: Boolean = false
    ) : Attachment() {
        override val value: String = phone
        override val typeName: String = TYPE_NAME

        companion object {
            const val TYPE_NAME = "PHONE"
        }
    }
}