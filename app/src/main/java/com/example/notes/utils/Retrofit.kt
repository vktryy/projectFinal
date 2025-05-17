package com.example.notes.utils

import com.example.notes.data.NotesApi
import com.example.notes.data.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://your-api-url.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun createNotesApi(): NotesApi {
        return retrofit.create(NotesApi::class.java)
    }

    fun createAuthApi(): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
