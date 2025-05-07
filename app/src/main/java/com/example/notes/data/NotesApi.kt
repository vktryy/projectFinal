package com.example.notes.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotesApi {
    @GET("notes")
    suspend fun getNotes(): List<Note>

    @POST("notes")
    suspend fun addNote(@Body note: Note)

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}
