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

    private val authToken = "test_token_123"

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
                val response = api.getNotes(authToken)

                // Проверяем успешность запроса
                if (response.isSuccessful) {
                    response.body()?.let { remoteNotes ->
                        // Сохраняем в базу и обновляем UI
                        dao.insertAll(remoteNotes)
                        _notes.value = dao.getAll()
                    }
                } else {
                    _error.value = "Ошибка сервера: ${response.code()}"
                    _notes.value = dao.getAll()
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
                _notes.value = dao.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                // Удаляем заметку через API (передаем токен и ID заметки)
                val response = api.deleteNote(
                    token = authToken,
                    noteId = note.id
                )

                if (response.isSuccessful) {
                    dao.delete(note)
                    _notes.value = dao.getAll()
                } else {
                    _error.value = "Ошибка сервера при удалении: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети при удалении: ${e.message}"
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