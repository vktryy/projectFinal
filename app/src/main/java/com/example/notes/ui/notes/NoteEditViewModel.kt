package com.example.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDatabase
import com.example.notes.data.NotesApi
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.launch

class EditNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: NoteDao = NotesDatabase.getInstance(application).noteDao()
    private val api: NotesApi = RetrofitClient.createNotesApi()

    fun saveNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (note.id.isBlank()) {
                    val createdNote = api.addNote(note)
                    dao.insert(createdNote)
                } else {
                    api.updateNote(note.id, note)
                    dao.update(note)
                }
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                api.deleteNote(note.id)
                dao.delete(note)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EditNoteViewModel(application) as T
                }
            }
        }
    }
}