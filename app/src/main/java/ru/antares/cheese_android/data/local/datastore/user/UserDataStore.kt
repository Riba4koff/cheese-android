package ru.antares.cheese_android.data.local.datastore.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.token.appDataStore
import ru.antares.cheese_android.data.local.models.LocalResponse

data class User(
    val surname: String,
    val name: String,
    val patronymic: String,
    val email: String,
    val phone: String,
    val birthday: String
)

interface IUserDataStore {
    val user: Flow<User>

    suspend fun save(user: User): LocalResponse<Unit>
    suspend fun clear(): LocalResponse<Unit>
}

class UserDataStore(
    private val context: Context
): IUserDataStore {

    private companion object KEYS {
        val surname = stringPreferencesKey("SURNAME_KEY")
        val name = stringPreferencesKey("NAME_KEY")
        val patronymic = stringPreferencesKey("PATRONYMIC_KEY")
        val birthday = stringPreferencesKey("BIRTHDAY_KEY")
        val phone = stringPreferencesKey("PHONE_KEY")
        val email = stringPreferencesKey("EMAIL_KEY")
    }

    private val dataStore = context.appDataStore

    override val user: Flow<User> = dataStore.data.map { preferences ->
        val surname = preferences[surname] ?: ""
        val name = preferences[name] ?: ""
        val patronymic = preferences[patronymic] ?: ""
        val birthday = preferences[birthday] ?: ""
        val phone = preferences[phone] ?: ""
        val email = preferences[email] ?: ""

        User(
            surname = surname,
            name = name,
            patronymic = patronymic,
            birthday = birthday,
            phone = phone,
            email = email
        )
    }

    override suspend fun save(user: User): LocalResponse<Unit> = try {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[surname] = user.surname
                preferences[name] = user.name
                preferences[patronymic] = user.patronymic
                preferences[birthday] = user.birthday
                preferences[phone] = user.phone
                preferences[email] = user.email
            }
            LocalResponse.Success(Unit)
        }
    } catch (e: Exception) {
        LocalResponse.Error("Не удалось сохранить данные пользователя")
    }

    override suspend fun clear(): LocalResponse<Unit> = try {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[surname] = ""
                preferences[name] = ""
                preferences[patronymic] = ""
                preferences[birthday] = ""
                preferences[phone] = ""
                preferences[email] = ""
            }
            LocalResponse.Success(Unit)
        }
    } catch (e: Exception) {
        LocalResponse.Error("Не удалось очистить данные пользователя")
    }
}