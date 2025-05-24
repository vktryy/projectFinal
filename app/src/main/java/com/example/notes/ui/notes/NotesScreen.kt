package com.example.notes.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.data.Note
import com.example.notes.ui.notes.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var currentNote by remember { mutableStateOf<Note?>(null) }
    var dialogId by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogContent by remember { mutableStateOf("") }

    LaunchedEffect(currentNote) {
        dialogTitle = currentNote?.title ?: ""
        dialogContent = currentNote?.content ?: ""
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Заметки") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    currentNote = null
                    dialogTitle = ""
                    dialogContent = ""
                    showDialog = true
                },
                modifier = Modifier
                    .padding(16.dp),
                containerColor = Color.Black
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopStart
            ) {
                Text("Нажмите плюсик")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = {
                            currentNote = note
                            showDialog = true
                        }
                    )
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(currentNote?.let { "Редактировать" } ?: "Новая") },
            text = {
                Column {
                    OutlinedTextField(
                        value = dialogTitle,
                        onValueChange = { dialogTitle = it },
                        label = { Text("Заголовок") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dialogContent,
                        onValueChange = { dialogContent = it },
                        label = { Text("Содержание") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentNote != null) {
                            viewModel.updateNote(
                                currentNote!!.copy(
                                    title = dialogTitle,
                                    content = dialogContent
                                )
                            )
                        } else {
                            viewModel.addNote(
                                Note(
                                    id = dialogId,
                                    title = dialogTitle,
                                    content = dialogContent
                                )
                            )
                        }
                        showDialog = false
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                if (currentNote != null) {
                    Button(
                        onClick = {
                            viewModel.deleteNote(currentNote!!)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        )
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}