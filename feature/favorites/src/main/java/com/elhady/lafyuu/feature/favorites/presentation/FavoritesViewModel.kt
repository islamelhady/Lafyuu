package com.elhady.lafyuu.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.favorites.R
import com.elhady.lafyuu.feature.favorites.domain.repository.FavoriteProductsRepository
import com.elhady.lafyuu.feature.favorites.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteProductsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FavoritesUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<FavoritesUiEffect> = _uiEffect.asSharedFlow()

    init {
        repository.observeFavorites()
            .onEach { products ->
                _uiState.update {
                    it.copy(isLoading = false, products = products.map { product -> product.toUi() })
                }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: FavoritesUiIntent) {
        when (intent) {
            FavoritesUiIntent.BackClicked -> sendEffect(FavoritesUiEffect.NavigateBack)
            is FavoritesUiIntent.ProductClicked ->
                sendEffect(FavoritesUiEffect.NavigateToProductDetails(intent.productId))
            is FavoritesUiIntent.RemoveFavoriteClicked -> removeFavorite(intent.productId)
        }
    }

    private fun removeFavorite(productId: String) {
        viewModelScope.launch {
            repository.removeFavorite(productId)
            _uiEffect.emit(
                FavoritesUiEffect.ShowSnackbar(
                    message = UiText.Resource(R.string.removed_from_favorite),
                    type = AlertType.Success
                )
            )
        }
    }

    private fun sendEffect(effect: FavoritesUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}