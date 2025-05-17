package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Insert
    suspend fun insertAll(notes: List<Note>)

    @Insert
    fun insert(note: Unit)

    @Update
    suspend fun update(note: Note)

    @Delete
    fun delete(note: Note)
}
