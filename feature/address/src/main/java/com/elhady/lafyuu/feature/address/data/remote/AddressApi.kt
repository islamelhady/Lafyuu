package com.elhady.lafyuu.feature.address.data.remote

import com.elhady.lafyuu.feature.address.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.address.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.address.data.remote.model.UpdateAddressRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {

    @GET("api/addresses")
    suspend fun getAddresses(): List<AddressDto>

    @GET("api/addresses/{Id}")
    suspend fun getAddress(
        @Path("Id") id: String
    ): AddressDto

    @POST("api/addresses")
    suspend fun createAddress(
        @Body request: CreateAddressRequestDto
    ): AddressDto

    @PUT("api/addresses/{Id}")
    suspend fun updateAddress(
        @Path("Id") id: String,
        @Body request: UpdateAddressRequestDto
    ): AddressDto

    @DELETE("api/addresses/{Id}")
    suspend fun deleteAddress(
        @Path("Id") id: String
    )
}
