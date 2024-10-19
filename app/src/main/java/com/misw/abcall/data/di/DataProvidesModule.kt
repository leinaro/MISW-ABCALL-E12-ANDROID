package com.misw.abcall.data.di

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

}

