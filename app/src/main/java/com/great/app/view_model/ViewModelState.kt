package com.great.app.view_model

sealed class ViewModelState {
    object Loading : ViewModelState()
    data class Error(val messageResId: Int) : ViewModelState()
    object Completed : ViewModelState()
}