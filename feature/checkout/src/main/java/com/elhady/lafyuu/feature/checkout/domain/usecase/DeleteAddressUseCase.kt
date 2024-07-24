package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(addressId: String): AppResult<Unit> {
        if (addressId.isBlank()) {
            return AppResult.Error("Invalid address ID")
        }
        return repository.deleteAddress(addressId)
    }
}
