package ru.antares.cheese_android.data.remote.services.community

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:15
 * Android Studio
 */

enum class GetCommunityError: CheeseError {
    SERVER_ERROR,
    NO_INTERNET_ERROR,
    UNKNOWN_ERROR,
    LOAD_ERROR
}