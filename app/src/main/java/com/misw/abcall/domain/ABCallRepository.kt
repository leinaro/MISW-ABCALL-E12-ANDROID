package com.misw.abcall.domain

import kotlinx.coroutines.flow.StateFlow

interface ABCallRepository {

    val isRefreshing: StateFlow<Boolean>
    val isInternetAvailable: StateFlow<Boolean>

}
