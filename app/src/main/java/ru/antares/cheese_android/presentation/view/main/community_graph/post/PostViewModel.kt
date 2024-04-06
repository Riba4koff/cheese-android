package ru.antares.cheese_android.presentation.view.main.community_graph.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.models.toUIModel
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.domain.repository.ICommunityRepository
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityScreenState
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.loading
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.post
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.products

/**
 * PostViewModel.kt
 * @author Павел
 * Created by on 06.04.2024 at 23:44
 * Android studio
 */

class PostViewModel(
    private val communityRepository: ICommunityRepository,
    private val cartRepository: ICartRepository,
    cart: GetCartFlowUseCase,
    private val postID: String
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<PostScreenState> =
        MutableStateFlow(PostScreenState())
    val state: StateFlow<PostScreenState> = combine(
        _mutableStateFlow, cart.entitites
    ) { state, cart ->
        state.copy {
            PostScreenState.products set (state.products.map { product ->
                product.copy(
                    countInCart = cart.find {
                        it.productID == product.value.id
                    }?.amount ?: 0
                )
            })
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), PostScreenState()
    )

    private val _navigationEvents = Channel<PostNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            communityRepository.get(postID).collect { receivingPost ->
                receivingPost.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            PostScreenState.loading set loading
                        }
                    }
                }.onError { error ->
                    /* TODO: ... */
                }.onSuccess { post ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            PostScreenState.post set post
                            PostScreenState.products set post.products.map { it.toUIModel() }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.AddProductToCart -> addToCart(event.productID, event.amount)
            is PostEvent.RemoveProductFromCart -> removeFromCart(event.productID, event.amount)
        }
    }

    fun onNavigationEvent(event: PostNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun addToCart(productID: String, amount: Int) {
        viewModelScope.launch {
            delay(1000)
            cartRepository.increment(
                productID = productID,
                currentAmount = amount
            ).collectLatest { resource ->
                resource.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            PostScreenState.loadingCart set loading
                        }
                    }
                }
            }
        }
    }

    private fun removeFromCart(productID: String, amount: Int) {
        viewModelScope.launch {
            cartRepository.decrement(
                productID = productID,
                currentAmount = amount
            ).collectLatest { resource ->
                resource.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            PostScreenState.loadingCart set loading
                        }
                    }
                }
            }
        }
    }
}