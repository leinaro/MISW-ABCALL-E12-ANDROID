package com.misw.abcall.data

import com.misw.abcall.domain.ABCallRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ABCallRepositoryImpl @Inject constructor(): ABCallRepository {
    private val _isRefreshing = MutableStateFlow(false)
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isInternetAvailable = MutableStateFlow(true)
    override val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable
}
