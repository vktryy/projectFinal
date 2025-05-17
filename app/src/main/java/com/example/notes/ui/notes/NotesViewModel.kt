package com.example.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesDatabase
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = NotesDatabase.getInstance(application).noteDao()
    private val api = RetrofitClient.createNotesApi()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadNotes()
    }

    fun loadNotes() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val remoteNotes = api.getNotes()
                dao.insertAll(remoteNotes)
                val localNotes = dao.getAll()
                _notes.value = localNotes
            } catch (e: Exception) {
                _error.value = e.message
                _notes.value = dao.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                api.deleteNote(note.id)
                dao.delete(note)
                _notes.value = dao.getAll()
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
            }
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(application) as T
                }
            }
        }
    }
}