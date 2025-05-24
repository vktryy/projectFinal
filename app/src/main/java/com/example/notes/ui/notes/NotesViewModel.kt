package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes
    private var nextId = 1

    fun addNote(note: Note) {
        viewModelScope.launch {
            _notes.value = _notes.value + note
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            _notes.value = _notes.value.map { note ->
                if (note.id == updatedNote.id) updatedNote else note
            }
        }
    }

    fun deleteNote(noteToDelete: Note) {
        viewModelScope.launch {
            _notes.value = _notes.value.filter { it.id != noteToDelete.id }
        }
    }
}