package com.misw.abcall.data.di

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.misw.abcall.data.api.ABCallApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:5008/"
//private const val BASE_URL = "http://34.8.156.167/"
//private const val BASE_URL = "https://api-gateway-649096178068.us-central1.run.app/"

@Module
@InstallIn(SingletonComponent::class)
object DataProvidesModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(BODY)
        val client: OkHttpClient = Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Provides
    @Singleton
    fun providesABCallApi(
        retrofit: Retrofit,
    ): ABCallApi = retrofit.create(ABCallApi::class.java)

    @Provides
    fun provideNetworkRquest() = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    @Provides
    fun providesFirebaseMessaging() = FirebaseMessaging.getInstance()
}

