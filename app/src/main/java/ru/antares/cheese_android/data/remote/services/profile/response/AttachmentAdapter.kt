package ru.antares.cheese_android.data.remote.services.profile.response

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class AttachmentAdapter : TypeAdapter<Attachment>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Attachment?) {
        // Мы не будем реализовывать запись, так как это используется только для чтения
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Attachment? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        reader.beginObject()
        var type: String? = null
        var phone: String? = null
        var email: String? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> type = reader.nextString()
                "phone" -> phone = reader.nextString()
                "email" -> email = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (type != null) {
            return when (type) {
                "PHONE" -> Attachment.Phone(phone ?: "", verified = false)
                "EMAIL" -> Attachment.Email(email ?: "", verified = false)
                else -> null
            }
        }

        return null
    }
}