package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class CreateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(input: AddressInput): AppResult<Address> {
        val clean = input.normalized()
        if (clean.validate().isNotEmpty()) {
            return AppResult.Failure(AppError.Validation("Invalid address"))
        }
        return repository.createAddress(clean)
    }
}