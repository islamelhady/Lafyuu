package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(addressId: String): AppResult<Unit> {
        if (addressId.isBlank()) {
            return AppResult.Failure(AppError.Validation("Invalid address ID"))
        }
        return repository.deleteAddress(addressId)
    }
}