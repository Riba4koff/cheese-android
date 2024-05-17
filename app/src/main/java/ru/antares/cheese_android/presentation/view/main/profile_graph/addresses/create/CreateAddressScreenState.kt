package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

import androidx.compose.runtime.Immutable
import arrow.optics.optics

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 17:59
 * Android Studio
 */

@optics
@Immutable
data class CreateAddressScreenState(
    val loading: Boolean = false,
    val city: String = "",
    val street: String = "",
    val house: String = "",
    val building: String = "",
    val entrance: String = "",
    val apartment: String = "",
    val floor: String = "",
    val comment: String = "",
) { companion object }