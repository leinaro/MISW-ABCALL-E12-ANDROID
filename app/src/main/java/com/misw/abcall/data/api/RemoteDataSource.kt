package com.misw.abcall.data.api

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val incidentApi: ABCallApi,
) {
    suspend fun searchIncident(id:String) = incidentApi.getIncidentById(id)
    suspend fun getUserIncidents(id:String) = incidentApi.getIncidentByUser(id)
    suspend fun getUser(id:String) = incidentApi.getUser(id)
}
