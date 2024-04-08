package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 16:02
 * Android Studio
 */

@optics
@Immutable
data class AddressesScreenState(
    val loading: Boolean = true,
    val loadingNextPage: Boolean = false,
    val addresses: List<AddressModel> = emptyList(),
    val endReached: Boolean = false,
    val currentPage: Int = 0,

) { companion object }