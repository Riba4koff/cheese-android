package ru.antares.cheese_android.presentation.view.main.community_graph.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.repository.ICommunityRepository

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:45
 * Android Studio
 */

class CommunityViewModel(
    private val repository: ICommunityRepository
) : ViewModel() {
    private val _mutableState: MutableStateFlow<CommunityScreenState> =
        MutableStateFlow(CommunityScreenState())
    val state: StateFlow<CommunityScreenState> = _mutableState.asStateFlow()

    private val _navigationEvents = Channel<CommunityNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            onEvent(CommunityEvent.LoadNextPage(null, null))
        }
    }

    fun onNavigationEvent(event: CommunityNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(event)
    }

    fun onEvent(event: CommunityEvent) = viewModelScope.launch {
        when (event) {
            is CommunityEvent.LoadNextPage -> loadNextPage(event.page, event.size)
        }
    }

    private fun loadNextPage(page: Int?, size: Int?) = viewModelScope.launch {
        repository.get(
            page = page,
            size = size,
            hasActivity = true
        ).collectLatest { result ->
            result.onLoading { loading ->
                _mutableState.update { it.copy(loading = loading) }
            }.onError { error ->
                Log.d("CommunityViewModel", error.toString())
            }.onSuccess { pagination ->
                val posts = pagination.result

                _mutableState.update { state ->
                    state.copy(
                        posts = posts
                    )
                }
            }
        }
    }
}