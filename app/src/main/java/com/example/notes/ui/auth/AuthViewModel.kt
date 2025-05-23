package com.example.notes.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.AuthApi
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authApi = RetrofitClient.createAuthApi()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()  // Успешный вход/регистрация
        data class Error(val message: String) : AuthState()
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun register(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    val response = authApi.register(
                        AuthApi.SignupModel(username, password)
                    )
                    if (response.isSuccessful) {
                        _state.value = AuthState.Success
                    } else {
                        handleError(response.code())
                    }
                } catch (e: Exception) {
                    _state.value = AuthState.Error("Ошибка сети: ${e.message}")
                }
            }
        }
    }

    fun login(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    val response = authApi.login(
                        AuthApi.SigninModel(username, password)
                    )
                    if (response.isSuccessful) {
                        _state.value = AuthState.Success
                    } else {
                        handleError(response.code())
                    }
                } catch (e: Exception) {
                    _state.value = AuthState.Error("Ошибка сети: ${e.message}")
                }
            }
        }
    }

    private fun handleError(code: Int) {
        _state.value = AuthState.Error(
            when (code) {
                400 -> "Некорректные данные"
                401 -> "Неверные учетные данные"
                409 -> "Пользователь уже существует"
                else -> "Ошибка сервера ($code)"
            }
        )
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