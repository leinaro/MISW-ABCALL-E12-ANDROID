package com.misw.abcall.ui.state

data class MainViewState(val name: String = "Android")

sealed class ABCallEvent{
    object Idle: ABCallEvent()
}