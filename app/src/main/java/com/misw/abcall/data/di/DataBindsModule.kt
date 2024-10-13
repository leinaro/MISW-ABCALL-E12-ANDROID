package com.misw.abcall.data.di

import com.misw.abcall.data.NetworkConnectivityService
import com.misw.abcall.data.NetworkConnectivityServiceImpl
import com.misw.abcall.domain.ABCallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindsModule {

    @Binds
    abstract fun bindNetworkConnectivityService(
        networkConnectivityServiceImpl: NetworkConnectivityServiceImpl,
    ): NetworkConnectivityService
}