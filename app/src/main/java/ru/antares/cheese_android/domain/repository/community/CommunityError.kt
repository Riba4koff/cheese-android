package ru.antares.cheese_android.domain.repository.community

import ru.antares.cheese_android.domain.errors.CheeseError

/**
 * CommunityError.kt
 * @author Павел
 * Created by on 02.04.2024 at 19:32
 * Android studio
 */

enum class CommunityError: CheeseError {
    SERVER_ERROR,
    NO_INTERNET_ERROR,
    UNKNOWN_ERROR,
    RECEIVE_ERROR
}