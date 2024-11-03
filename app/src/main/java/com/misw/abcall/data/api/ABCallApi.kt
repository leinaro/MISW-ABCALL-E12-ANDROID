package com.misw.abcall.data.api

import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.domain.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ABCallApi {
    @GET("/incidents/{id}")
    suspend fun getIncidentById(@Path("id") id: String?): IncidentDTO
    @GET("/incidents/user/{id}")
    suspend fun getIncidentByUser(@Path("id") id: String?): List<IncidentDTO>
    @GET("/user/{id}")
    suspend fun getUser(@Path("id") id: String?): UserDTO
    @POST("/chatbot")
    suspend fun chat(@Body message: ChatMessageDTO): ChatMessageResponseDTO
}

data class ChatMessageDTO(
    val chatbot_conversation_id: String?=null,
    val message: String,
)

data class ChatMessageResponseDTO(
    val msg: String?=null,
    val chatbot_conversation_id: String?=null,
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
