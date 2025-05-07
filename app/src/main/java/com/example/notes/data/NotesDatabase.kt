package com.example.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "notes_db"
            ).build().also { instance = it }
        }
    }
}
