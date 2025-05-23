package com.example.notes.ui.notes

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.data.Note
import java.util.UUID

@Composable
fun EditNoteScreen(
    note: Note? = null,
    onBack: () -> Unit,
    viewModel: EditNoteViewModel = viewModel(
        factory = EditNoteViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.text ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Содержание") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Назад")
            }

            if (note != null) {
                Button(
                    onClick = {
                        viewModel.deleteNote(
                            Note(
                                id = note.id,
                                title = title,
                                text = content
                            )
                        ) {
                            onBack()
                        }
                    }
                ) {
                    Text("Удалить")
                }
            }

            Button(
                onClick = {
                    viewModel.saveNote(
                        Note(
                            id = note?.id ?: UUID.randomUUID().toString(),
                            title = title,
                            text = content
                        )
                    ) {
                        onBack()
                    }
                }
            ) {
                Text(if (note == null) "Создать" else "Сохранить")
            }
        }
    }
}
