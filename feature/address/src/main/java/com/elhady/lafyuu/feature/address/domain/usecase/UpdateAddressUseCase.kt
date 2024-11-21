package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(addressId: String, input: AddressInput): AppResult<Address> {
        if (addressId.isBlank()) {
            return AppResult.Failure(AppError.Validation("Invalid address ID"))
        }
        val clean = input.normalized()
        if (clean.validate().isNotEmpty()) {
            return AppResult.Failure(AppError.Validation("Invalid address"))
        }
        return repository.updateAddress(addressId, clean)
    }
}