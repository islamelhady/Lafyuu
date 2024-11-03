package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.first

internal suspend fun CartRepository.currentCart(): AppResult<Cart> =
    observeCart().first()?.let { AppResult.Success(it) } ?: getCart()