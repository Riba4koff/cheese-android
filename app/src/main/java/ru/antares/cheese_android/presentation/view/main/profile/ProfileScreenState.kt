package ru.antares.cheese_android.presentation.view.main.profile

import androidx.compose.runtime.Composable
import ru.antares.cheese_android.domain.errors.ProfileUIError


sealed interface ProfileScreenState {
    data object LoadingState : ProfileScreenState
    data class AuthorizedState(
        val surname: String? = null,
        val name: String? = null,
        val patronymic: String? = null,
    ) : ProfileScreenState

    data object NonAuthorizedState: ProfileScreenState

    data class ErrorState(val error: ProfileUIError) : ProfileScreenState

    @Composable
    fun onAuthorizedState(content: @Composable (data: AuthorizedState) -> Unit): ProfileScreenState {
        return if (this is AuthorizedState) {
            content(this)
            this
        } else this
    }

    @Composable
    fun onNonAuthorizedState(content: @Composable () -> Unit): ProfileScreenState {
        return if (this is NonAuthorizedState) {
            content()
            this
        } else this
    }

    @Composable
    fun onLoadingState(content: @Composable () -> Unit): ProfileScreenState {
        return if (this is LoadingState) {
            content()
            this
        } else this
    }

    @Composable
    fun onErrorState(content: @Composable (error: ProfileUIError) -> Unit): ProfileScreenState {
        return if (this is ErrorState) {
            content(this.error)
            this
        } else this
    }
}