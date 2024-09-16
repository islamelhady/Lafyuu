package com.elhady.lafyuu.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<FavoritesUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeFavorites()
    }

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            FavoritesUiEvent.LoadFavorites -> observeFavorites()
            is FavoritesUiEvent.RemoveFavorite -> removeFavorite(event.productId)
            is FavoritesUiEvent.ProductClicked -> sendEffect(FavoritesUiEffect.NavigateToProductDetails(event.productId))
            FavoritesUiEvent.BackClicked -> sendEffect(FavoritesUiEffect.NavigateBack)
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            favoritesRepository.observeFavoriteIds().collectLatest { favoriteIds ->
                if (favoriteIds.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            favoriteProducts = emptyList(),
                            error = null
                        )
                    }
                } else {
                    val deferredProducts = favoriteIds.map { id ->
                        async {
                            when (val result = getProductDetailsUseCase(id)) {
                                is AppResult.Success -> result.data.details
                                else -> null
                            }
                        }
                    }
                    val products = deferredProducts.awaitAll().filterNotNull()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            favoriteProducts = products,
                            error = null
                        )
                    }
                }
            }
        }
    }

    private fun removeFavorite(productId: String) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(productId)
            sendEffect(FavoritesUiEffect.ShowSnackbar("Removed from Favorite", AlertType.Success))
        }
    }

    private fun sendEffect(effect: FavoritesUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
