package com.example.notes.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(
        @Body request: AuthRequest
    ): Response<AuthResponse>
    @POST("auth/login")
    suspend fun login(
        @Body request: AuthRequest
    ): Response<AuthResponse>
}

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String
)