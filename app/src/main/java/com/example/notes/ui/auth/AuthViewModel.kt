package com.example.notes.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.AuthRequest
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authApi = RetrofitClient.createAuthApi()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun register(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    val response = authApi.register(AuthRequest(username, password))
                    if (response.isSuccessful) {
                        _state.value = AuthState.Success(response.body()?.token ?: "")
                    } else {
                        _state.value = AuthState.Error(
                            when (response.code()) {
                                400 -> "Некорректные данные"
                                409 -> "Пользователь уже существует"
                                else -> "Ошибка сервера"
                            }
                        )
                    }
                } catch (e: Exception) {
                    _state.value = AuthState.Error("Нет подключения к интернету")
                }
            }
        }
    }

    fun login(username: String, password: String) {
        if (validateInput(username, password)) {
            _state.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    val response = authApi.login(AuthRequest(username, password))
                    if (response.isSuccessful) {
                        _state.value = AuthState.Success(response.body()?.token ?: "")
                    } else {
                        _state.value = AuthState.Error(
                            when (response.code()) {
                                401 -> "Неверный логин или пароль"
                                else -> "Ошибка сервера"
                            }
                        )
                    }
                } catch (e: Exception) {
                    _state.value = AuthState.Error("Нет подключения к интернету")
                }
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
            password.length < 6 -> {
                _state.value = AuthState.Error("Пароль должен содержать ≥6 символов")
                false
            }
            else -> true
        }
    }
}