package com.misw.abcall.data.api

import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.domain.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ABCallApi {
    @GET("/incidents/{id}")
    suspend fun getIncidentById(@Path("id") id: String?): IncidentDTO
    @GET("/incidents/user/{id}")
    suspend fun getIncidentByUser(@Path("id") id: String?): List<IncidentDTO>
    @GET("/user/{id}")
    suspend fun getUser(@Path("id") id: String?): UserDTO
}
