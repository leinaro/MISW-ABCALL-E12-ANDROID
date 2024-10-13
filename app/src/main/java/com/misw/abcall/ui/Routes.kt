package com.misw.abcall.ui

sealed class Routes(val path:String){
    data object SearchIncident : Routes("search-incident")
    data object IncidentDetails : Routes("incident/{incidentId}")
    data object UserIncidentList : Routes("user/{userId}/incident")
    data object UserDetails : Routes("user/{userId}")
}
