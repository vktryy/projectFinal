package com.example.notes.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.data.Note
import com.example.notes.theme.Purple80
import com.example.notes.theme.PurpleGrey40
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID

private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var currentNote by remember { mutableStateOf<Note?>(null) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogContent by remember { mutableStateOf("") }
    var dialogCategory by remember { mutableStateOf("") }


    LaunchedEffect(currentNote) {
        dialogTitle = currentNote?.title ?: ""
        dialogContent = currentNote?.content ?: ""
        dialogCategory = currentNote?.category ?: ""
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Заметки") }) },
        floatingActionButton = {
            Button(
                onClick = {
                    currentNote = null
                    dialogTitle = ""
                    dialogContent = ""
                    dialogCategory = ""

                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Добавить заметку")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Нажмите Добавить заметку")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                // Сортируем заметки по дате создания (новые сверху)
                items(notes.sortedByDescending { it.createdAt }) { note ->
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
            title = { Text(currentNote?.let { "Редактировать" } ?: "Новая заметка") },
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
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dialogCategory,
                        onValueChange = { dialogCategory = it },
                        label = { Text("Категория") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    currentNote?.let { note ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Создано: ${dateFormatter.format(Date(note.createdAt))}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentNote != null) {
                            viewModel.updateNote(
                                currentNote!!.copy(
                                    title = dialogTitle,
                                    content = dialogContent,
                                    category = dialogCategory
                                )
                            )
                        } else {
                            viewModel.addNote(
                                Note(
                                    id = UUID.randomUUID().toString(),
                                    title = dialogTitle,
                                    content = dialogContent,
                                    category = dialogCategory
                                    // createdAt будет установлено автоматически
                                )
                            )
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                Row {
                    if (currentNote != null) {
                        Button(
                            onClick = {
                                viewModel.deleteNote(currentNote!!)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Удалить")
                        }
                    }
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Magenta,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Отмена")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Категория с ярким фоном
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color(0xFFFF5722), // Оранжевый для всех
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = note.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = dateFormatter.format(Date(note.createdAt)),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}