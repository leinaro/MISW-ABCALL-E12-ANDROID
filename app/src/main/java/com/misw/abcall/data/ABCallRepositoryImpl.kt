package com.misw.abcall.data

import android.R
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.misw.abcall.data.api.ChatMessageDTO
import com.misw.abcall.data.api.ChatMessageResponseDTO
import com.misw.abcall.data.api.LocalDataSource
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
    private val localDataSource: LocalDataSource,
    private val networkConnectivityService: NetworkConnectivityService,
    private val firebaseMessaging: FirebaseMessaging,
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
                        else -> {}
                    }
                }
        }
    }

    private fun subscribe(id: String) {
        firebaseMessaging.subscribeToTopic(id)
            .addOnCompleteListener { task ->
               // var msg: String? = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                 //  msg = getString(R.string.msg_subscribe_failed)
                }
                //Log.d(TAG, msg!!)
                //Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }
    }

    override fun getIncident(query: String): Flow<IncidentDTO> {
        return flow {
            try {
                _isRefreshing.value = true
                val incident = remoteDataSource.searchIncident(query)
                emit(incident)
                incident.id?.let { subscribe(it) }
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
                val incident = remoteDataSource.getUserIncidents(query)
                emit(incident)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }
    }

    override fun start(): Flow<ChatMessageResponseDTO> {
        return flow {
            try {
                _isRefreshing.value = true
                val response = remoteDataSource.startChat()
                emit(response)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }
    }

    override fun chat(message: ChatMessageDTO): Flow<String> {
        return flow {
            try {
                _isRefreshing.value = true
                val response = remoteDataSource.chat(message)
                response.msg?.let {
                    emit(it)
                }?: run {
                    response.id?.let {
                        emit("He creado el incidente con el id $it y se encuentra en estado ${response.status}" )
                        subscribe(it)
                    }
                }
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
                val user = remoteDataSource.getUser(query)
                emit(user)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _isRefreshing.value = false
                e.toUiError()
            }
        }
    }

    override fun getSelectedLanguage(): Flow<String?> {
        return localDataSource.getSelectedLanguage()
    }

    override suspend fun updateSelectedLanguage(code: String) {
        return localDataSource.updateSelectedLanguage(code)
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