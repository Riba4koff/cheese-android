package ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 20.05.2024 at 14:47
 * Android Studio
 */

@optics
@Immutable
data class SelectAddressScreenState(
    val loading: Boolean = true,
    val loadingNextPage: Boolean = false,
    val addresses: List<AddressModel> = listOf(),
    val address: AddressModel? = null,
    val currentPage: Int = 0,
    val endReached: Boolean = false
) { companion object }