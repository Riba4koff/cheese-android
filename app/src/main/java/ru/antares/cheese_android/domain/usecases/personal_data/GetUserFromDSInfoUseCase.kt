package ru.antares.cheese_android.domain.usecases.personal_data

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User

class GetUserFromDSInfoUseCase(
    userDS: IUserDataStore
) {
    val value: Flow<User> = userDS.user
}