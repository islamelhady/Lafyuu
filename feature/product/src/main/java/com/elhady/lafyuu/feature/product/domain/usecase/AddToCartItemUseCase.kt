package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

}