package com.misw.abcall.data

import com.misw.abcall.data.api.RemoteDataSource
import com.misw.abcall.domain.ABCallRepository
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.domain.UserDTO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ABCallRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkConnectivityService: NetworkConnectivityService,
): ABCallRepository {
    private val _isRefreshing = MutableStateFlow(false)
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isInternetAvailable = MutableStateFlow(true)
    override val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable

    init {
        _isInternetAvailable.value = networkConnectivityService.networkConnection()
        GlobalScope.launch{
            networkConnectivityService.networkStatus
                .collect {
                    when(it) {
                        NetworkStatus.Unknown -> _isInternetAvailable.value = true
                        NetworkStatus.Connected -> _isInternetAvailable.value = true
                        NetworkStatus.Disconnected -> _isInternetAvailable.value = false
                    }
                }
        }
    }

    override fun getIncident(query: String): Flow<IncidentDTO> {
        return flow {
            try {
                _isRefreshing.value = true
              //  emit(localDataSource.getAlbums())
                val incident = remoteDataSource.searchIncident(query)
                emit(incident)
               // val albumsEntityList = albums.map { it.toEntity() }
               // localDataSource.insertAlbums(albumsEntityList)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }
    }

    override fun getUserIncidents(query: String): Flow<List<IncidentDTO>> {
        return flow {
            try {
                _isRefreshing.value = true
                //  emit(localDataSource.getAlbums())
                val incident = remoteDataSource.getUserIncidents(query)
                emit(incident)
                // val albumsEntityList = albums.map { it.toEntity() }
                // localDataSource.insertAlbums(albumsEntityList)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }

    }

    override fun getUser(query: String): Flow<UserDTO> {
        return flow {
            try {
                _isRefreshing.value = true
               // emit(localDataSource.getAlbums())
                val albums = remoteDataSource.getUser(query)
                emit(albums)
               // val albumsEntityList = albums.map { it.toEntity() }
              //  localDataSource.insertAlbums(albumsEntityList)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }
    }

}

fun Exception.toUiError() {
    this.printStackTrace()
    when (this) {
        is ConnectException -> throw UIError.NetworkError
        is SocketException -> throw UIError.ServerError
        else -> throw UIError.UnknownError
    }
}

sealed class UIError(
    val userMessage: String,
): Exception(userMessage) {
    object NetworkError : UIError(userMessage = "Estás en modo ermitaño digital. Sin internet.")
    object ServerError : UIError(userMessage = "¡Ups! El servidor está tomando un café. Intenta nuevamente en breve.")
    object UnknownError : UIError(userMessage = "¡Uh-oh! Algo ha tomado un camino inusual. Llama a nuestro equipo de desarrollo.")
}