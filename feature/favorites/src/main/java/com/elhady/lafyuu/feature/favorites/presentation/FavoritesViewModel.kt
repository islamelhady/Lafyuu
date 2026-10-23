package com.elhady.lafyuu.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.domain.repository.ProductRepository
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.favorites.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<FavoritesUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val reloadTrigger = MutableStateFlow(0)

    init {
        observeFavorites()
    }

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            FavoritesUiEvent.LoadFavorites -> reloadTrigger.update { it + 1 }
            is FavoritesUiEvent.RemoveFavorite -> removeFavorite(event.productId)
            is FavoritesUiEvent.ProductClicked -> sendEffect(FavoritesUiEffect.NavigateToProductDetails(event.productId))
            FavoritesUiEvent.BackClicked -> sendEffect(FavoritesUiEffect.NavigateBack)
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.observeFavoriteIds()
                .combine(reloadTrigger) { favoriteIds, trigger -> Pair(favoriteIds, trigger) }
                .collectLatest { (favoriteIds, _) ->
                    if (favoriteIds.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                favoriteProducts = emptyList(),
                                error = null
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                            val deferredProducts = favoriteIds.map { id ->
                                async {
                                    when (val result = productRepository.getProductDetails(id)) {
                                        is AppResult.Success -> result.data
                                        else -> null
                                    }
                                }
                            }
                            val loadedProducts = deferredProducts.awaitAll().filterNotNull()

                            if (loadedProducts.isEmpty() && favoriteIds.isNotEmpty()) {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = UiText.Resource(R.string.failed_to_load_favorite_products)
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        favoriteProducts = loadedProducts,
                                        error = null
                                    )
                                }
                            }
                    }
                }
        }
    }

    private fun removeFavorite(productId: String) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(productId)
            sendEffect(FavoritesUiEffect.ShowSnackbar(message = UiText.Resource(R.string.removed_from_favorite), type = AlertType.Success))
        }
    }

    private fun sendEffect(effect: FavoritesUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
