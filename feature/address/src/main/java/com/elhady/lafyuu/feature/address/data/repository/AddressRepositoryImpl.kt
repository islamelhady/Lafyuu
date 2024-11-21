package com.elhady.lafyuu.feature.address.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.address.data.mapper.toCreateRequest
import com.elhady.lafyuu.feature.address.data.mapper.toDomain
import com.elhady.lafyuu.feature.address.data.mapper.toUpdateRequest
import com.elhady.lafyuu.feature.address.data.remote.AddressApi
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi,
    private val safeApiCaller: SafeApiCaller
) : AddressRepository {

    override suspend fun getAddresses(): AppResult<List<Address>> = safeApiCaller.call {
        // an address without an id would crash the list (duplicate keys), so it is dropped
        addressApi.getAddresses().filter { !it.id.isNullOrBlank() }.map { it.toDomain() }
    }

    override suspend fun getAddress(addressId: String): AppResult<Address> =
        safeApiCaller.call { addressApi.getAddress(addressId).toDomain() }

    override suspend fun createAddress(input: AddressInput): AppResult<Address> =
        safeApiCaller.call { addressApi.createAddress(input.toCreateRequest()).toDomain() }

    override suspend fun updateAddress(addressId: String, input: AddressInput): AppResult<Address> =
        safeApiCaller.call {
            addressApi.updateAddress(addressId, input.toUpdateRequest(addressId)).toDomain()
        }

    override suspend fun deleteAddress(addressId: String): AppResult<Unit> =
        safeApiCaller.call { addressApi.deleteAddress(addressId) }
}