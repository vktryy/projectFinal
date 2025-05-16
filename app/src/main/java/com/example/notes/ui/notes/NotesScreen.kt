package com.example.notes.ui.notes

import androidx.compose.runtime.LaunchedEffect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.data.Note
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Открыть экран создания */ }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }
    }
}