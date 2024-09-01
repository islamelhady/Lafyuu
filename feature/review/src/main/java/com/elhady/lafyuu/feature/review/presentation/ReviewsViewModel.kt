package com.elhady.lafyuu.feature.review.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.review.domain.usecase.GetProductReviewsInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val getProductReviewsInfoUseCase: GetProductReviewsInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productIdArg: String = savedStateHandle["productId"] ?: ""

    private val _uiState = MutableStateFlow(ReviewsUiState(productId = productIdArg))
    val uiState: StateFlow<ReviewsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ReviewsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (productIdArg.isNotBlank()) {
            loadReviews(productIdArg)
        }
    }

    fun onEvent(event: ReviewsUiEvent) {
        when (event) {
            is ReviewsUiEvent.LoadReviews -> loadReviews(event.productId)
            is ReviewsUiEvent.FilterRating -> {
                _uiState.update { it.copy(selectedRatingFilter = event.rating) }
            }
            ReviewsUiEvent.WriteReviewClicked -> {
                if (productIdArg.isNotBlank()) {
                    sendEffect(ReviewsUiEffect.NavigateToWriteReview(productIdArg))
                }
            }
            ReviewsUiEvent.BackClicked -> sendEffect(ReviewsUiEffect.NavigateBack)
            ReviewsUiEvent.RetryClicked -> loadReviews(productIdArg)
        }
    }

    private fun loadReviews(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getProductReviewsInfoUseCase(productId)) {
                is AppResult.Success -> {
                    val info = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            averageRating = info.averageRating,
                            reviewsCount = info.reviewsCount,
                            reviews = info.reviews,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to load reviews"
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                    sendEffect(ReviewsUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: ReviewsUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
