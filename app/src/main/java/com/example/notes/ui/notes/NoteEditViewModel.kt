package com.example.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDatabase
import kotlinx.coroutines.launch
import java.util.UUID

class NoteEditViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: NoteDao = NotesDatabase.getInstance(application).noteDao()

    fun saveNote(note: Note, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                if (note.id.isBlank()) {
                    val newNote = note.copy(id = UUID.randomUUID().toString())
                    dao.insert(newNote)
                } else {
                    dao.update(note)
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteNote(note: Note, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                dao.delete(note)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NoteEditViewModel(application) as T
                }
            }
        }
    }
}