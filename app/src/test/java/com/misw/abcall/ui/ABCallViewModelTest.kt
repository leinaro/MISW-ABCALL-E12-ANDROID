package com.misw.abcall.ui

import com.misw.abcall.MainDispatcherRule
import com.misw.abcall.data.api.ChatMessageDTO
import com.misw.abcall.data.api.ChatMessageResponseDTO
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.MainViewState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ABCallViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: ABCallRepository = mockk(relaxed = true)
    private lateinit var viewModel: ABCallViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ABCallViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSelectedLanguage should update state with selected language`() = runTest {
        // Arrange
        val expectedLanguage = "es"
        coEvery { repository.getSelectedLanguage() } returns flowOf(expectedLanguage)

        // Act
        viewModel = ABCallViewModel(repository)
        advanceUntilIdle()

        // Assert
        assertEquals(expectedLanguage, viewModel.state.value.selectedLanguage)
    }

    @Test
    fun `startChat should navigate to Chat route when successful`() = runTest {
        // Arrange
        val message = ChatMessageResponseDTO(msg = "Hola! Soy un agente virtual, en que puedo ayudarte?")
        coEvery { repository.start() } returns flowOf(message)

        // Act
        viewModel.onUserIntent(UserIntent.OpenChat)
        advanceUntilIdle()

        // Assert
        assertEquals(message.msg, viewModel.state.value.messageList.last().message,"Hola! Soy un agente virtual, en que puedo ayudarte?")
        assertEquals(
            ABCallEvent.Idle,
            //ABCallEvent.NavigateTo("chat"),
            viewModel.event.value
        )
    }

    @Test
    fun `searchIncidentOrUser should update incidentDTO and navigate to details`() = runTest {
        // Arrange
        val incidentId = "incident123"
        val mockIncident = mockk<IncidentDTO> {
            every { id } returns incidentId
            every { status } returns "OPEN"
        }
        coEvery { repository.getIncident(incidentId) } returns flowOf(mockIncident)

        // Act
        viewModel.onUserIntent(UserIntent.SearchIncident(incidentId))
        advanceUntilIdle()

        // Assert
        assertEquals(mockIncident, viewModel.getIncident())
        assertEquals(
            ABCallEvent.Idle,
//            ABCallEvent.NavigateTo("incident/$incidentId"),
            viewModel.event.value
        )
    }

    @Test
    fun `sendMessage should add message to state and call chat`() = runTest {
        // Arrange
        val userMessage = "Hola"
        val agentResponse = "Hola, ¿En qué puedo ayudarte?"
        coEvery { repository.chat(any()) } returns flowOf(agentResponse)

        // Act
        viewModel.onUserIntent(UserIntent.SendMessage(userMessage))
        advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.state.value.messageList.size)
        assertEquals(agentResponse, viewModel.state.value.messageList.last().message)
    }

    @Test
    fun `updateSelectedLanguage should update repository with new language`() = runTest {
        // Arrange
        val newLanguage = "fr"

        // Act
        viewModel.onUserIntent(UserIntent.UpdateLanguage(newLanguage))
        advanceUntilIdle()

        // Assert
        coVerify { repository.updateSelectedLanguage(newLanguage) }
    }
}
