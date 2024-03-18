package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.AppError

@optics
@Immutable
data class ProfileState(
    val surname: String = "",
    val name: String = "",
    val patronymic: String = "",
    val isLoading: Boolean = true,
    val profileLoaded: Boolean = false,
    val isAuthorized: Boolean = false,
    val error: AppError? = null
) { companion object }