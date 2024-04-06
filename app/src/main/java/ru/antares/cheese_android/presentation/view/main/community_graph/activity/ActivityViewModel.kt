package ru.antares.cheese_android.presentation.view.main.community_graph.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.domain.TimestampParser
import ru.antares.cheese_android.domain.models.toUIModel
import ru.antares.cheese_android.domain.repository.ICommunityRepository
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * CommunityDetailViewModel.kt
 * @author Павел
 * Created by on 06.04.2024 at 19:51
 * Android studio
 */

class ActivityViewModel(
    private val repository: ICommunityRepository,
    private val cartRepository: CartRepository,
    private val parser: TimestampParser,
    cart: GetCartFlowUseCase,
    private val postID: String
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ActivityScreenState> =
        MutableStateFlow(ActivityScreenState())
    val state: StateFlow<ActivityScreenState> =
        _mutableStateFlow.combine(cart.entitites) { state, cart ->
            val startFrom = parser(state.post?.activityModel?.startFrom)

            state.copy(
                products = state.products.map { product ->
                    product.copy(
                        countInCart = cart.find {
                            it.productID == product.value.id
                        }?.amount ?: 0,
                    )
                },
                post = state.post?.copy(
                    activityModel = state.post.activityModel?.copy(
                        startFrom = startFrom
                    )
                )
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ActivityScreenState()
        )

    private val _navigationEvents = Channel<ActivityNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.get(postID).collect { receivingPost ->
                receivingPost.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ActivityScreenState.loading set loading
                        }
                    }
                }.onError { error ->
                    /* TODO: ... */
                }.onSuccess { post ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ActivityScreenState.post set post
                            ActivityScreenState.products set post.products.map { it.toUIModel() }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ActivityEvent) {
        when (event) {
            is ActivityEvent.BuyTicket -> buyTicket(event.activityID)
            is ActivityEvent.AddProductToCart -> addProductToCart(
                event.productID,
                event.amount
            )

            is ActivityEvent.RemoveProductFromCart -> removeProductFromCart(
                event.productID,
                event.amount
            )
        }
    }

    fun onNavigationEvent(event: ActivityNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun buyTicket(activityID: String) {
        viewModelScope.launch {

        }
    }

    private fun addProductToCart(productID: String, amount: Int) {
        viewModelScope.launch {
            cartRepository.increment(
                productID = productID,
                currentAmount = amount
            ).collectLatest { resource ->
                resource.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ActivityScreenState.loadingCart set loading
                        }
                    }
                }
            }
        }
    }

    private fun removeProductFromCart(productID: String, amount: Int) {
        viewModelScope.launch {
            cartRepository.decrement(
                productID = productID,
                currentAmount = amount
            ).collectLatest { resource ->
                resource.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ActivityScreenState.loadingCart set loading
                        }
                    }
                }
            }
        }
    }
}