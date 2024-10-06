package com.misw.abcall.domain.di

import com.misw.abcall.data.ABCallRepositoryImpl
import com.misw.abcall.domain.ABCallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainBindsModule {

    @Binds
    abstract fun bindRepository(
        repositoryImpl: ABCallRepositoryImpl,
    ): ABCallRepository

    /*        @Binds
            abstract fun bindNetworkConnectivityService(
                networkConnectivityServiceImpl: NetworkConnectivityServiceImpl,
            ): NetworkConnectivityService*/
}