package com.elhady.lafyuu.feature.address.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput

interface AddressRepository {
    suspend fun getAddresses(): AppResult<List<Address>>
    suspend fun getAddress(addressId: String): AppResult<Address>
    suspend fun createAddress(input: AddressInput): AppResult<Address>
    suspend fun updateAddress(addressId: String, input: AddressInput): AppResult<Address>
    suspend fun deleteAddress(addressId: String): AppResult<Unit>
}