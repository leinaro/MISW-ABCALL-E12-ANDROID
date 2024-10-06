package com.misw.abcall.ui

import androidx.lifecycle.ViewModel
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
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


}
