package com.example.notes.data

import retrofit2.Response
import retrofit2.http.*

interface NotesApi {
    @GET("api/note")
    suspend fun getNotes(
        @Header("Authorization") token: String
    ): Response<List<Note>>

    @GET("api/note/{note_id}")
    suspend fun getNoteById(
        @Header("Authorization") token: String,
        @Path("note_id") noteId: String
    ): Response<Note>

    @POST("api/note")
    suspend fun addNote(
        @Header("Authorization") token: String,
        @Body newNote: NewNoteModel
    ): Response<NoteIdResponse>

    @PATCH("api/note/{note_id}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("note_id") noteId: String,
        @Body patchNote: PatchNoteModel
    ): Response<NoteIdResponse>

    @DELETE("api/note/{note_id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("note_id") noteId: String
    ): Response<Unit>

    data class NewNoteModel(
        val title: String,
        val text: String
    )

    data class PatchNoteModel(
        val title: String?,
        val text: String?
    )

    data class NoteIdResponse(
        val note_id: String
    )
}
