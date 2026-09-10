package com.elhady.lafyuu.feature.auth.di

import com.elhady.lafyuu.core.network.auth.TokenRemoteDataSource
import com.elhady.lafyuu.core.network.di.NoAuth
import com.elhady.lafyuu.feature.auth.data.remote.AuthApi
import com.elhady.lafyuu.feature.auth.data.remote.RefreshApi
import com.elhady.lafyuu.feature.auth.data.remote.TokenRemoteDataSourceImpl
import com.elhady.lafyuu.feature.auth.data.repository.AuthRepositoryImpl
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenRemoteDataSource(tokenRemoteDataSourceImpl: TokenRemoteDataSourceImpl): TokenRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @Provides
        @Singleton
        fun provideRefreshApi(@NoAuth retrofit: Retrofit): RefreshApi {
            return retrofit.create(RefreshApi::class.java)
        }
    }
}
