package com.misw.abcall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.Routes.IncidentDetails
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.ABCallEvent.NavigateTo
import com.misw.abcall.ui.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    private fun searchIncidentOrUser(query: String) {
        val mutex = Mutex()
        viewModelScope.launch {
            mutex.withLock {
                repository.getIncident(query)
                    .catch {
                        setEvent(
                            ABCallEvent.ShowError(it.message ?: "Error al consultar datos")
                        )
                    }
                    .collect { incident ->
                        /*setState(
                            state.value.copy(
                                incident = incident,
                            )
                        )*/
                        incidentDTO = incident
                        setEvent(
                            NavigateTo(IncidentDetails.path.replace("{incidentId}",incident.id.orEmpty() ))
                        )
                    }

                _state.value = MainViewState("Android")

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
            }

    }

    fun getIncident() = incidentDTO
}

sealed class UserIntent{
    data class SearchIncident(val query: String): UserIntent()

}
