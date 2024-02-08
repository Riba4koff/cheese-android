package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.antares.cheese_android.domain.errors.UIError


sealed interface ProfileScreenState {
    data object LoadingState : ProfileScreenState
    data class AuthorizedState(
        val surname: String? = null,
        val name: String? = null,
        val patronymic: String? = null,
    ) : ProfileScreenState

    data object NonAuthorizedState : ProfileScreenState

    data class ErrorState(val error: ProfileUIError) : ProfileScreenState
}