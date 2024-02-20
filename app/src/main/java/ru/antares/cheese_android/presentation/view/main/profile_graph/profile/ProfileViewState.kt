package ru.antares.cheese_android.presentation.view.main.profile_graph.profile


sealed interface ProfileViewState {
    val key: ProfileViewStateKey
    data class LoadingState(
        override val key: ProfileViewStateKey = ProfileViewStateKey.LOADING
    ): ProfileViewState
    data class AuthorizedState(
        val surname: String? = null,
        val name: String? = null,
        val patronymic: String? = null,
        override val key: ProfileViewStateKey = ProfileViewStateKey.AUTHORIZED
    ) : ProfileViewState

    data class NonAuthorizedState(
        override val key: ProfileViewStateKey = ProfileViewStateKey.NON_AUTHORIZED
    ): ProfileViewState

    data class ErrorState(
        val error: ProfileUIError,
        override val key: ProfileViewStateKey = ProfileViewStateKey.ERROR
    ) : ProfileViewState
}