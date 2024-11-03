package com.misw.abcall.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.NetworkRequest.Builder
import com.misw.abcall.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class NetworkConnectivityServiceImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val connectivityManager = mockk<ConnectivityManager>(relaxed = true)
  //  private val networkCapabilities = mockkStatic(NetworkCapabilities::class)
    private val networkRequest = mockk<NetworkRequest>()
    private val mockBuilder = mockk<NetworkRequest.Builder>(relaxed = true)

    private val context: Context = mockk()
    private lateinit var subject: NetworkConnectivityService

    @Before
    fun setUp() {
        val builder = mockk<Builder>()
        //every { Builder() } returns builder
      //  every { networkCapabilities } returns builder
        every { mockBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns mockBuilder
        every { mockBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI) } returns mockBuilder
        every { mockBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) } returns mockBuilder

        every { builder.addCapability(any()) } returns builder
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        subject = NetworkConnectivityServiceImpl(context, networkRequest)
    }


    @Test
    fun networkConnection() {
        // Given
    //    val network = mockk<ConnectivityManager.NetworkCallback>()
    //    every { connectivityManager.activeNetwork }
    //    val networkCapabilities = mockk<ConnectivityManager.NetworkCapabilities>()

        // When
        val result = subject.networkConnection()

        // Then
        assert(result == false)
    }

    @Ignore
    @Test
    fun getNetworkStatus()= runTest {
        // Given

        // When
        val result = subject.networkStatus.last()

        // Then
        assert(result == NetworkStatus.Unknown)
    }
}