package com.elhady.lafyuu.feature.review.presentation.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.review.domain.usecase.CreateReviewUseCase
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
class WriteReviewViewModel @Inject constructor(
    private val createReviewUseCase: CreateReviewUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productIdArg: String = savedStateHandle["productId"] ?: ""

    private val _uiState = MutableStateFlow(WriteReviewUiState(productId = productIdArg))
    val uiState: StateFlow<WriteReviewUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<WriteReviewUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: WriteReviewUiEvent) {
        when (event) {
            is WriteReviewUiEvent.RatingChanged -> {
                _uiState.update { it.copy(rating = event.rating) }
            }
            is WriteReviewUiEvent.CommentChanged -> {
                _uiState.update { it.copy(comment = event.comment) }
            }
            WriteReviewUiEvent.SubmitClicked -> submitReview()
            WriteReviewUiEvent.BackClicked -> sendEffect(WriteReviewUiEffect.NavigateBack)
        }
    }

    private fun submitReview() {
        val state = _uiState.value
        if (state.productId.isBlank()) return
        if (state.comment.isBlank()) {
            sendEffect(WriteReviewUiEffect.ShowSnackbar("Comment cannot be empty", AlertType.Error))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = createReviewUseCase(state.productId, state.rating, state.comment)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(WriteReviewUiEffect.ShowSnackbar("Review submitted successfully", AlertType.Success))
                    sendEffect(WriteReviewUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(WriteReviewUiEffect.ShowSnackbar(result.message ?: "Failed to submit review", AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: WriteReviewUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
