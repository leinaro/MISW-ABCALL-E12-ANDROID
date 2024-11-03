package com.misw.abcall.ui.state

import com.misw.abcall.ui.chat.ChatMessage

data class MainViewState(
    val name: String = "Android",
    val messageList: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val selectedLanguage: String? = null,
)

sealed class ABCallEvent{
    data class NavigateTo(val route:String): ABCallEvent()
    object NavigateBack: ABCallEvent()
    data class ShowError(val message: String): ABCallEvent()
    data class ShowSuccess(val message: String): ABCallEvent()
    object Idle: ABCallEvent()
}