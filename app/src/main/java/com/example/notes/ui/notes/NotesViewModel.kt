package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.notes.data.Note
import com.example.notes.data.NoteDao

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val noteDao: NoteDao? = App.getDatabase()?.noteDao()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            val notesList = noteDao!!.getAll()
            _notes.value = notesList
        }
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao!!.insert(note)
            loadNotes()
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            noteDao!!.update(updatedNote)
            loadNotes()
        }
    }

    fun deleteNote(noteToDelete: Note) {
        viewModelScope.launch {
            noteDao!!.delete(noteToDelete)
            loadNotes()
        }
    }
}