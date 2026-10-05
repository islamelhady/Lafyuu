package com.elhady.lafyuu.feature.profile.di

import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.profile.data.remote.ProfileApi
import com.elhady.lafyuu.feature.profile.data.repository.ProfileRepositoryImpl
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileApi: ProfileApi,
        apiErrorParser: ApiErrorParser
    ): ProfileRepository {
        return ProfileRepositoryImpl(profileApi, apiErrorParser)
    }
}
