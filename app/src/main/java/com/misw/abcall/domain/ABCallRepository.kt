package com.misw.abcall.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ABCallRepository {
    fun getIncident(query: String): Flow<IncidentDTO>
    fun getUserIncidents(query: String): Flow<List<IncidentDTO>>
    fun getUser(query: String): Flow<UserDTO>

    val isRefreshing: StateFlow<Boolean>
    val isInternetAvailable: StateFlow<Boolean>

}

data class IncidentDTO(
    val id: String? = null,
    val agent_id_creation: String? = null,
    val description: String? = null,
    val date: String? = null,
    val registration_medium: String? = null,
    val user_id: Int? = null,
    val status: String? = null,
    val agent_id_last_update: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val client_id: String? = null
)

data class UserDTO(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
)
