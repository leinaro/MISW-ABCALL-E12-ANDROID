package com.misw.abcall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.abcall.data.api.ChatMessageDTO
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.Routes.IncidentDetails
import com.misw.abcall.ui.chat.ChatMessage
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.ABCallEvent.NavigateTo
import com.misw.abcall.ui.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ABCallViewModel @Inject constructor(
    private val repository: ABCallRepository,
    ): ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> get() = _state


    private val _event: MutableStateFlow<ABCallEvent> = MutableStateFlow(ABCallEvent.Idle)
    val event: StateFlow<ABCallEvent> get() = _event

    val isRefreshing: StateFlow<Boolean> = repository.isRefreshing
    var isInternetAvailable: StateFlow<Boolean> = repository.isInternetAvailable

    var incidentDTO: IncidentDTO? = null

    private lateinit var chatUUID: String

   private fun startChat(){
       viewModelScope.launch {
           repository.start()
               .catch {
                   setEvent(
                       ABCallEvent.ShowError(it.message ?: "No se pudo iniciar la conversación")
                   )
               }
               .collect {
                   setEvent(NavigateTo(Routes.Chat.path))
                     chatUUID = it
                   _state.update { state ->
                       state.copy(
                           isTyping = false,
                           messageList = state.messageList + ChatMessage()
                       )
                   }
               }

       }
   }

    private fun chat(message: String) {
        val mutex = Mutex()
        viewModelScope.launch {
            mutex.withLock {
                repository.chat(ChatMessageDTO(
                    chatUUID, message
                ))
                    .catch {
                        _state.update { state ->
                            state.copy(
                                isTyping = false,
                                messageList = state.messageList + ChatMessage(
                                    message = "Lo siento no pude realizar esta operación",
                                    isAgent = true,
                                )
                            )
                        }
                    }
                    .collect { message ->
                        _state.update { state ->
                            state.copy(
                                isTyping = false,
                                messageList = state.messageList + ChatMessage(
                                    message = message,
                                    isAgent = true,
                                )
                            )
                        }
                    }
            }
        }
    }

    private fun searchIncidentOrUser(query: String, isChatBot: Boolean = false) {
        val mutex = Mutex()
        viewModelScope.launch {
            mutex.withLock {
                repository.getIncident(query)
                    .catch {
                        if (isChatBot){
                            _state.update { state ->
                                state.copy(
                                    isTyping = false,
                                    messageList = state.messageList + ChatMessage(
                                        message = "Lo siento no encuentro un incidente con ese id.",
                                        isAgent = true,
                                    )
                                )
                            }
                        } else{
                            setEvent(
                                ABCallEvent.ShowError(it.message ?: "Error al consultar datos")
                            )
                        }

                    }
                    .collect { incident ->
                        /*setState(
                            state.value.copy(
                                incident = incident,
                            )
                        )*/
                        incidentDTO = incident
                        if (!isChatBot){
                            setEvent(
                                NavigateTo(IncidentDetails.path.replace("{incidentId}",incident.id.orEmpty() ))
                            )
                        }
                        else {
                            _state.update { state ->
                                state.copy(
                                    isTyping = false,
                                    messageList = state.messageList + ChatMessage(
                                        message = "El incidente ${incident.id} se encuentra en estado ${incident.status}",
                                        isAgent = true,
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun setState(newState: MainViewState) {
        _state.value = newState
    }

    fun setEvent(event: ABCallEvent) {
        viewModelScope.launch {
            //mutex.withLock {
            _event.value = event
            //}
        }
    }

    fun onUserIntent(userIntent: UserIntent) {
            when (userIntent) {
                is UserIntent.SearchIncident -> {
                    searchIncidentOrUser(userIntent.query)
                }
                is UserIntent.ActivateChat -> {
                    startChat()
                }
                is UserIntent.StartChat -> {
                    setEvent(NavigateTo(Routes.ActivateChat.path))
                }
                is UserIntent.SendMessage -> {
                    _state.update { state ->
                        state.copy(
                            isTyping = true,
                            messageList = state.messageList + ChatMessage(
                                message = userIntent.message,
                                isAgent = false,
                            )
                        )
                    }
                    if (userIntent.message.contains("consultar", true)) {
                        viewModelScope.launch {
                            delay(1000)
                            _state.update { state ->
                                state.copy(
                                    isTyping = false,
                                    messageList = state.messageList + ChatMessage(
                                        message = "Escribe el número de incidente de quieres consultar",
                                        isAgent = true,
                                    )
                                )
                            }
                        }
                    }
                    else if(isUUID(userIntent.message)){
                        viewModelScope.launch {
                            searchIncidentOrUser(userIntent.message, isChatBot = true)
                        }
                    }
                   /* else if(userIntent.message.contains("crear", true)){
                        viewModelScope.launch {
                            chat(userIntent.message)
                        }
                    }*/
                    else if (userIntent.message.contains("hola", true)) {
                        viewModelScope.launch {
                            _state.update { state ->
                                state.copy(
                                    isTyping = false,
                                    messageList = state.messageList + ChatMessage(
                                        message = "Hola, ¿En qué puedo ayudarte?",
                                        isAgent = true,
                                    )
                                )
                            }
                        }

                    } else {
                        viewModelScope.launch {
                            chat(userIntent.message)
                        }
                        /*viewModelScope.launch {
                            delay(1000)
                            _state.update { state ->
                                state.copy(
                                    isTyping = false,
                                    messageList = state.messageList + ChatMessage(
                                        message = "No entiendo tu mensaje",
                                        isAgent = true,
                                    )
                                )
                            }
                        }*/
                    }
                }
            }

    }

    fun getIncident() = incidentDTO
}

sealed class UserIntent{
    data class SearchIncident(val query: String): UserIntent()
    object ActivateChat: UserIntent()
    object StartChat: UserIntent()
    data class SendMessage(val message: String): UserIntent()
}

fun isUUID(input: String): Boolean {
    return try {
        UUID.fromString(input)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}