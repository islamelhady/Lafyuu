package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(): AppResult<List<Address>> = repository.getAddresses()
}
