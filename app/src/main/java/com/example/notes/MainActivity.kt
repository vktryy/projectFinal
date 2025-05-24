package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.notes.data.NotesDatabase
import com.example.notes.theme.NotesTheme
import com.example.notes.ui.auth.AuthScreen
import com.example.notes.ui.notes.NotesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotesDatabase.getInstance(applicationContext)

        setContent {
            NotesTheme {
                var isAuthenticated by remember { mutableStateOf(false) }

                if (isAuthenticated) {
                    NotesScreen(
                        onAddNote = { println("Добавить заметку") },
                        onNoteClick = { note -> println("Нажата заметка: ${note.title}") },
                    )
                } else {
                    AuthScreen(
                        onAuthSuccess = { isAuthenticated = true }
                    )
                }
            }
        }
    }
}