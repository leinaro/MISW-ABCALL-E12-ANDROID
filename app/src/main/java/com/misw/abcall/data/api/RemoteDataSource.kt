package com.misw.abcall.data.api

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val incidentApi: ABCallApi,
) {
    suspend fun searchIncident(id:String) = incidentApi.getIncidentById(id)
    suspend fun getUserIncidents(id:String) = incidentApi.getIncidentByUser(id)
    suspend fun getUser(id:String) = incidentApi.getUser(id)
    suspend fun chat(message: ChatMessageDTO): ChatMessageResponseDTO = incidentApi.chat(message)
    suspend fun startChat(): String {
        val chatbotid = incidentApi.chat(ChatMessageDTO(message = "start")).chatbot_conversation_id.orEmpty()
        incidentApi.chat(ChatMessageDTO(chatbot_conversation_id = chatbotid, message = "1"))
        return chatbotid
    }
}
