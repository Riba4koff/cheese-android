package ru.antares.cheese_android.presentation.view.main.home_graph.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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
import ru.antares.cheese_android.data.repository.main.CommunityRepository
import ru.antares.cheese_android.data.repository.main.ProductsRepository
import ru.antares.cheese_android.domain.TimestampParser
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartState
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.cartLoading
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.error
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityScreenState
import ru.antares.cheese_android.presentation.view.main.community_graph.community.posts

/**
 * HomeScreenViewModel.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:38
 * Android studio
 */

class HomeScreenViewModel(
    getCartFlowUseCase: GetCartFlowUseCase,
    private val productsRepository: ProductsRepository,
    private val communityRepository: CommunityRepository,
    private val parser: TimestampParser,
    private val repository: CartRepository
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = combine(
        _mutableStateFlow,
        getCartFlowUseCase.products
    ) { state, products ->
        state.copy {
            HomeScreenState.recommendations set state.recommendations.map { recommendation ->
                recommendation.copy(
                    countInCart = products.find { value ->
                        value.product.id == recommendation.value.id
                    }?.amount ?: 0
                )
            }
            HomeScreenState.activities set (state.activities.map {
                it.copy(
                    activityModel = it.activityModel?.copy(
                        startFrom = parser(it.activityModel.startFrom)
                    )
                )
            })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _mutableStateFlow.value
    )

    private val _navigationEvents: Channel<HomeScreenNavigationEvent> = Channel()
    val navigationEvents: Flow<HomeScreenNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        onEvent(HomeScreenRecommendationsEvent.LoadNextPage(0, 16))
        loadCommunity()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenRecommendationsEvent.AddToCart -> addRecommendationToCart(
                event.id,
                event.amount
            )

            is HomeScreenRecommendationsEvent.RemoveFromCart -> removeRecommendationFromCart(
                event.id,
                event.amount
            )

            is HomeScreenRecommendationsEvent.LoadNextPage -> loadNextPageOfRecommendation(
                page = event.page,
                size = event.size
            )
        }
    }

    fun onNavigationEvent(event: HomeScreenNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun loadNextPageOfRecommendation(page: Int, size: Int) {
        viewModelScope.launch {
            productsRepository.get(recommend = true).collectLatest { resource ->
                resource.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.loadingRecommendations set if (loading) LoadingRecommendations.LoadingNextPage
                            else LoadingRecommendations.Loaded
                        }
                    }
                }.onSuccess { pagination ->
                    val mapped = pagination.result.map { product ->
                        ProductUIModel(value = product, countInCart = 0, isFavorite = false)
                    }

                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.recommendations set (state.recommendations + mapped)
                            HomeScreenState.loadingRecommendations set LoadingRecommendations.Loaded
                            HomeScreenState.recommendationsEndReached set (pagination.sizeResult <= size && pagination.amountOfPages - 1 == page)
                            HomeScreenState.currentRecommendationsPage set pagination.page
                        }
                    }
                }
            }
        }
    }

    private fun loadCommunity() {
        viewModelScope.launch {
            communityRepository.get().collectLatest {
                it.onLoading { loading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.loadingPosts set if (loading) LoadingPosts.LoadingNextPage
                            else LoadingPosts.Loaded
                        }
                    }
                }.onSuccess { pagination ->
                    Log.d("activities", pagination.result.filter { post -> post.activityModel == null }.size.toString())
                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.posts set pagination.result.filter { post -> post.activityModel == null }
                            HomeScreenState.activities set pagination.result.filter { activity -> activity.activityModel != null }
                            HomeScreenState.loadingPosts set LoadingPosts.Loaded
                            HomeScreenState.loadingActivities set LoadingActivities.Loaded
                        }
                    }
                }
            }
        }
    }

    private fun addRecommendationToCart(id: String, amount: Int) {
        viewModelScope.launch {
            repository.increment(amount, id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.loadingCart set isLoading
                        }
                    }
                }
            }
        }
    }

    private fun removeRecommendationFromCart(id: String, amount: Int) {
        viewModelScope.launch {
            repository.decrement(amount, id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            HomeScreenState.loadingCart set isLoading
                        }
                    }
                }
            }
        }
    }
}