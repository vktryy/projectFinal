package com.example.notes.ui.notes

package com.example.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesDatabase
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Дополнительно, если используете sealed class для состояний:
sealed class NotesState {
    object Loading : NotesState()
    data class Success(val notes: List<Note>) : NotesState()
    data class Error(val message: String) : NotesState()
}

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = NotesDatabase.getInstance(application).noteDao()
    private val api = RetrofitClient.instance

    val notes: LiveData<List<Note>> = dao.getAll()

    fun loadNotes() {
        viewModelScope.launch {
            try {
                val remoteNotes = api.getNotes()
                remoteNotes.forEach { dao.insert(it) }
            } catch (e: Exception) {
                // Ошибка сети - используем локальные данные
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                api.deleteNote(note.id)
                dao.delete(note)
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}