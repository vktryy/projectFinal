package com.example.notes.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/user/signup")
    suspend fun register(
        @Body request: SignupModel
    ): Response<Unit>

    @POST("api/user/signin")
    suspend fun login(
        @Body request: SigninModel
    ): Response<Unit>

    data class SignupModel(
        val username: String,
        val password: String
    )

    data class SigninModel(
        val username: String,
        val password: String
    )
}