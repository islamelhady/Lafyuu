package com.elhady.lafyuu.feature.address.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.address.domain.model.Address

interface AddressRepository {
    suspend fun getAddresses(): AppResult<List<Address>>
    suspend fun getAddress(addressId: String): AppResult<Address>
    suspend fun createAddress(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address>
    suspend fun updateAddress(
        addressId: String,
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address>
    suspend fun deleteAddress(addressId: String): AppResult<Unit>
}
