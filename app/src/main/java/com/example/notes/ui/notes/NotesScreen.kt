package com.example.notes.ui.notes

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
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Семья" -> Color(0xFFFF5722)
        "Отдых" -> Color(0xFF2196F3)
        "Работа" -> Color(0xFF4CAF50)
        else -> Color(0xFF9C27B0)
    }
}

fun getCategoryTextColor(category: String): Color {
    return when (category) {
        "Семья" -> Color.White
        "Отдых" -> Color.White
        "Работа" -> Color.White
        else -> Color.White
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState(emptyList())
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var currentNote by remember { mutableStateOf<Note?>(null) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogContent by remember { mutableStateOf("") }
    var dialogCategory by remember { mutableStateOf(Note.CATEGORIES.first()) }

    LaunchedEffect(currentNote) {
        dialogTitle = currentNote?.title ?: ""
        dialogContent = currentNote?.content ?: ""
        dialogCategory = currentNote?.category ?: Note.CATEGORIES.first()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Заметки")

                        Row {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { viewModel.setCategoryFilter(null) },
                                label = { Text("Все") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color.Gray,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.LightGray,
                                    labelColor = Color.Black
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            // Кнопки для каждой категории с цветами
                            Note.CATEGORIES.forEach { category ->
                                FilterChip(
                                    selected = selectedCategory == category,
                                    onClick = {
                                        viewModel.setCategoryFilter(
                                            if (selectedCategory == category) null else category
                                        )
                                    },
                                    label = {
                                        Text(category, color = getCategoryTextColor(category))
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = getCategoryColor(category),
                                        containerColor = getCategoryColor(category).copy(alpha = 0.2f),
                                        selectedLabelColor = getCategoryTextColor(category),
                                        labelColor = getCategoryColor(category)
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    currentNote = null
                    dialogTitle = ""
                    dialogContent = ""
                    dialogCategory = Note.CATEGORIES.first()
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
        val filteredNotes = if (selectedCategory != null) {
            notes.filter { it.category == selectedCategory }
        } else {
            notes
        }

        if (filteredNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    if (selectedCategory != null) "Нет заметок в категории '$selectedCategory'"
                    else "Нажмите Добавить заметку"
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(filteredNotes.sortedByDescending { it.createdAt }) { note ->
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
            title = { Text(if (currentNote != null) "Редактировать заметку" else "Новая заметка") },
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

                    Text(
                        text = "Категория:",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Column {
                        Note.CATEGORIES.forEach { category ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                RadioButton(
                                    selected = dialogCategory == category,
                                    onClick = { dialogCategory = category },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = getCategoryColor(category)
                                    )
                                )
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = getCategoryColor(category),
                                    fontWeight = if (dialogCategory == category) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

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

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = getCategoryColor(note.category),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = note.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = getCategoryTextColor(note.category),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
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