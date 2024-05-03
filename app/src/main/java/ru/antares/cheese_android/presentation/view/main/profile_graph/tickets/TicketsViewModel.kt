package ru.antares.cheese_android.presentation.view.main.profile_graph.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.repository.main.TicketsRepository

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 15:28
 * Android Studio
 */

class TicketsViewModel(
    private val ticketsRepository: TicketsRepository
): ViewModel() {
    private val _mutableState: MutableStateFlow<TicketsState> = MutableStateFlow(TicketsState())
    val state: StateFlow<TicketsState> = _mutableState.asStateFlow()

    private val _navigationEvents: Channel<TicketsNavigationEvents> = Channel(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onEvent(event: TicketsEvent) {
        when (event) {
            is TicketsEvent.LoadNextPage -> loadNextPage(event.page, event.size)
            is TicketsEvent.OnClickTicket -> onClickTicket(event.postId)
        }
    }

    fun onNavigationEvent(event: TicketsNavigationEvents) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun loadNextPage(page: Int, size: Int) {
        viewModelScope.launch {
            ticketsRepository.get(size, page).collect { tickets ->
                tickets.onLoading { loading ->
                    _mutableState.update { state ->
                        state.copy(
                            loadingNextPage = loading
                        )
                    }
                }.onError { error ->
                    /* TODO: make handling error */
                }.onSuccess { pagination ->
                    _mutableState.update { state ->
                        state.copy(
                            loading = false,
                            currentPage = pagination.page,
                            ticketsOfActivities = (state.ticketsOfActivities ?: emptyList()) + pagination.result,
                            endReached = (pagination.sizeResult <= size && pagination.amountOfPages - 1 == page)
                        )
                    }
                }
            }
        }
    }

    private fun onClickTicket(postId: String) {
        viewModelScope.launch {
            _navigationEvents.send(TicketsNavigationEvents.NavigateToActivity(postId))
        }
    }
}