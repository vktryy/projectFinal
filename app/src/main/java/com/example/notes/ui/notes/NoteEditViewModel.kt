package com.example.notes.ui.notes

package com.example.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesDatabase
import com.example.notes.utils.RetrofitClient
import kotlinx.coroutines.launch

class EditNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = NotesDatabase.getInstance(application).noteDao()
    private val api = RetrofitClient.instance.notesApi

    fun saveNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (note.id.isBlank()) {
                    // Новая заметка
                    val createdNote = api.addNote(note)
                    dao.insert(createdNote)
                } else {
                    // Редактирование существующей
                    api.updateNote(note.id, note)
                    dao.update(note)
                }
                onSuccess()
            } catch (e: Exception) {
                // Обработка ошибки (можно добавить StateFlow для ошибок)
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
}