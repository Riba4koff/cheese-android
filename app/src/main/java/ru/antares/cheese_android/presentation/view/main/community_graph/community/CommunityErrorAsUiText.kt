package ru.antares.cheese_android.presentation.view.main.community_graph.community

import ru.antares.cheese_android.R
import ru.antares.cheese_android.data.remote.api.main.community.CommunityError
import ru.antares.cheese_android.data.remote.api.main.community.CommunityError.*
import ru.antares.cheese_android.domain.UiText

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:40
 * Android Studio
 */

fun CommunityError.asUiText(): UiText = when (this) {
    SERVER_ERROR -> UiText.StringResource(R.string.server_error)
    NO_INTERNET_ERROR -> UiText.StringResource(R.string.no_internet_error)
    UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error)
    LOAD_ERROR -> UiText.StringResource(R.string.load_posts_error)
}