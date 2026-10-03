package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(): AppResult<List<Address>> {
        return repository.getAddresses()
    }
}
