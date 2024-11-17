package com.misw.abcall.data

import com.misw.abcall.MainDispatcherRule
import com.misw.abcall.data.api.LocalDataSource
import com.misw.abcall.data.api.RemoteDataSource
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.domain.UserDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.ConnectException
import java.net.SocketException

class ABCallRepositoryImplTest {


    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val remoteDataSource: RemoteDataSource  = mockk()
    private val networkConnectivityService: NetworkConnectivityService = mockk()
    private val localDataSource: LocalDataSource = mockk()
    private lateinit var subject: ABCallRepository

    @Before
    fun setUp() {
        coEvery { networkConnectivityService.networkConnection() } returns true
        coEvery { networkConnectivityService.networkStatus } returns flowOf(NetworkStatus.Connected)

        subject = ABCallRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkConnectivityService = networkConnectivityService,
        )
    }

    @Test
    fun searchIncident()= runTest {
        // Given
        val query = "1"
        val incident = mockk<IncidentDTO>()
        coEvery { remoteDataSource.searchIncident(query) } returns incident

        // When
        val result = subject.getIncident(query).last()

        // Then
        coVerify(exactly = 1) { remoteDataSource.searchIncident(query) }
        assert(result == incident)
    }

    @Test(expected = UIError.UnknownError::class)
    fun searchIncidentErrorUnknownError()= runTest {
        // Given
        val query = "1"
        coEvery { remoteDataSource.searchIncident(query) }.throws(Exception("Test"))


        // When
        subject.getIncident(query).last()


        // Then
        coVerify(exactly = 1) { remoteDataSource.searchIncident(query) }
    }

    @Test(expected = UIError.NetworkError::class)
    fun searchIncidentErrorNetworkError()= runTest {
        // Given
        val query = "1"
        coEvery { remoteDataSource.searchIncident(query) }.throws(ConnectException("Test"))


        // When
        subject.getIncident(query).last()


        // Then
        coVerify(exactly = 1) { remoteDataSource.searchIncident(query) }
    }

    @Test(expected = UIError.ServerError::class)
    fun searchIncidentErrorServerError()= runTest {
        // Given
        val query = "1"
        coEvery { remoteDataSource.searchIncident(query) }.throws(SocketException("Test"))

        // When
        subject.getIncident(query).last()


        // Then
        coVerify(exactly = 1) { remoteDataSource.searchIncident(query) }
    }



    @Test
    fun getUserIncidents()= runTest {
        // Given
        val query = "1"
        val incidents = listOf(mockk<IncidentDTO>())
        coEvery { remoteDataSource.getUserIncidents(query) } returns incidents

        // When
        val result = subject.getUserIncidents(query).last()
        // Then
        coVerify(exactly = 1) { remoteDataSource.getUserIncidents(query) }
        assert(result == incidents)
    }

    @Test
    fun getUser()= runTest {
        // Given
        val query = "1"
        val user = mockk<UserDTO>()
        coEvery { remoteDataSource.getUser(query) } returns user

        // When
        val result = subject.getUser(query).last()

        // Then
        coVerify(exactly = 1) { remoteDataSource.getUser(query) }
        assert(result == user)
    }
}