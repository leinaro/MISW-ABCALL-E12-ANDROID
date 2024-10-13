package com.misw.abcall.ui.state

data class MainViewState(val name: String = "Android")

sealed class ABCallEvent{
    data class NavigateTo(val route:String): ABCallEvent()
    object NavigateBack: ABCallEvent()
    data class ShowError(val message: String): ABCallEvent()
    data class ShowSuccess(val message: String): ABCallEvent()
    object Idle: ABCallEvent()
}