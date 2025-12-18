package com.example.notes

import android.app.Application
import androidx.room.Room
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "user-db"
        )
            .allowMainThreadQueries()
            .build()
    }

    companion object {
        private var db: NotesDatabase? = null
        fun getDatabase(): NotesDatabase? {
            return db
        }
        fun getNoteDao(): NoteDao {
            return db!!.noteDao()
        }
    }
}