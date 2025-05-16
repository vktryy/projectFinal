package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesApi
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val noteDao: NoteDao) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val api: NotesApi = RetrofitClient.instance

    init {
        loadNotes()
    }

    fun loadNotes() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val remoteNotes = api.getNotes()
                noteDao.insertAll(remoteNotes)
                _notes.value = noteDao.getAll()
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                _notes.value = noteDao.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                api.deleteNote(note.id)
                noteDao.delete(note)
                _notes.value = noteDao.getAll()
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
            }
        }
    }
}