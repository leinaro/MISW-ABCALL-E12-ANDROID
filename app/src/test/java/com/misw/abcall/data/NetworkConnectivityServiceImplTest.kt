package com.misw.abcall.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.NetworkRequest.Builder
import com.misw.abcall.MainDispatcherRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration

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
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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


    @Test
    fun `networkConnection returns true when network has TRANSPORT_WIFI`() {
        val network: Network = mockk()
        val capabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) } returns false

        val result = subject.networkConnection()

        assertEquals(true, result)
    }

    @Test
    fun `networkConnection returns true when network has TRANSPORT_CELLULAR`() {
        val network: Network = mockk()
        val capabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) } returns false

        val result = subject.networkConnection()

        assertEquals(true, result)
    }

    @Test
    fun `networkConnection returns true when network has TRANSPORT_VPN`() {
        val network: Network = mockk()
        val capabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) } returns true

        val result = subject.networkConnection()

        assertEquals(true, result)
    }

//---
    @Test
    fun `networkConnection returns false when no network is available`() {
        every { connectivityManager.activeNetwork } returns null

        val result = subject.networkConnection()

        assertEquals(false, result)
    }
/*
    @Test
    fun `networkStatus emits Connected when network becomes available`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        every { connectivityManager.registerNetworkCallback(networkRequest, capture(callbackSlot)) } just Runs
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } just Runs

        // Start collecting the flow
        val job = launch { subject.networkStatus.take(1).collect() }

        // Simulate network available
        val network: Network = mockk()
        callbackSlot.captured.onAvailable(network)

        // Wait for flow to emit
        advanceTimeBy(2000)
       // advanceUntilIdle()

        val status = subject.networkStatus.first()
        assertEquals(NetworkStatus.Connected, status)

        job.cancel()
    }

    @Test
    fun `networkStatus emits Disconnected when network is lost`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        every { connectivityManager.registerNetworkCallback(networkRequest, capture(callbackSlot)) } just Runs
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } just Runs

        val flow = subject.networkStatus

        // Simulate network lost
        val network: Network = mockk()
        callbackSlot.captured.onLost(network)

        val status = flow.take(1).first()
        assertEquals(NetworkStatus.Disconnected, status)
    }

    @Test
    fun `networkStatus emits Disconnected when network is unavailable`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

        every { connectivityManager.registerNetworkCallback(networkRequest, capture(callbackSlot)) } just Runs
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } just Runs

        val flow = subject.networkStatus

        // Simulate network unavailable
        callbackSlot.captured.onUnavailable()

        val status = flow.take(1).first()
        assertEquals(NetworkStatus.Disconnected, status)
    }*/

}