package com.example.notes.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun register(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                _state.value = AuthState.Success
            }
        }
    }

    fun login(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                _state.value = AuthState.Success
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isBlank() -> {
                _state.value = AuthState.Error("Введите логин")
                false
            }
            password.isBlank() -> {
                _state.value = AuthState.Error("Введите пароль")
                false
            }
            else -> true
        }
    }
}