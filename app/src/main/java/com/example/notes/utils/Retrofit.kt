package com.example.notes.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://your-api.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notesApi: NotesApi = retrofit.create(NotesApi::class.java)
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}
