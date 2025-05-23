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
    private val token = "test_token_123"

    fun saveNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (note.id.isBlank()) {
                    val response = api.addNote(
                        token = token,
                        newNote = NotesApi.NewNoteModel(
                            title = note.title,
                            text = note.text
                        )
                    )
                    if (response.isSuccessful) {
                        val createdNote = note.copy(id = response.body()?.note_id ?: "")
                        dao.insert(createdNote)
                        onSuccess()
                    }
                } else {
                    val response = api.updateNote(
                        token = token,
                        noteId = note.id,
                        patchNote = NotesApi.PatchNoteModel(
                            title = note.title,
                            text = note.text
                        )
                    )
                    if (response.isSuccessful) {
                        dao.update(note)
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.deleteNote(
                    token = token,
                    noteId = note.id
                )
                if (response.isSuccessful) {
                    dao.delete(note)
                    onSuccess()
                }
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