package com.misw.abcall.data.api

import com.misw.abcall.MainDispatcherRule
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.domain.UserDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val incidentApi: ABCallApi = mockk()
    private lateinit var subject: RemoteDataSource

    @Before
    fun setUp() {
        subject = RemoteDataSource(incidentApi)
    }

    @Test
    fun searchIncident()= runTest {
        // Given
        val id = "1"
        val incident = mockk<IncidentDTO>()
        coEvery { incidentApi.getIncidentById(id) } returns incident

        // When
        val result = subject.searchIncident(id)

        // Then
        coVerify(exactly = 1) { incidentApi.getIncidentById(id) }
        assert(result == incident)
    }

    @Test
    fun getUserIncidents()= runTest {
        // Given
        val id = "1"
        val incidents = listOf(mockk<IncidentDTO>())
        coEvery { incidentApi.getIncidentByUser(id) } returns incidents

        // When
        val result = subject.getUserIncidents(id)

        // Then
        coVerify(exactly = 1) { incidentApi.getIncidentByUser(id) }
        assert(result == incidents)
    }

    @Test
    fun getUser()= runTest {
        // Given
        val id = "1"
        val user = mockk<UserDTO>()
        coEvery { incidentApi.getUser(id) } returns user

        // When
        val result = subject.getUser(id)

        // Then
        coVerify(exactly = 1) { incidentApi.getUser(id) }
        assert(result == user)
    }

}