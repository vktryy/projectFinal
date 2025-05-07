package com.example.notes.ui.notes

package com.example.notes.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.R
import com.example.notes.data.Note
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note? = null,
    onBack: () -> Unit,
    viewModel: EditNoteViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditNoteViewModel(LocalContext.current.applicationContext as Application) as T
            }
        }
    )
) {
    var title by remember {
        mutableStateOf(TextFieldValue(note?.title ?: ""))
    }
    var content by remember {
        mutableStateOf(TextFieldValue(note?.content ?: ""))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (note == null) "Новая заметка" else "Редактировать") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (note != null) {
                        IconButton(
                            onClick = {
                                viewModel.deleteNote(
                                    Note(
                                        id = note.id,
                                        title = title.text,
                                        content = content.text,
                                        date = System.currentTimeMillis()
                                    )
                                ) {
                                    onBack()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    viewModel.saveNote(
                        Note(
                            id = note?.id ?: UUID.randomUUID().toString(),
                            title = title.text,
                            content = content.text,
                            date = System.currentTimeMillis()
                        )
                    ) {
                        onBack()
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Сохранить")
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Сохранить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
        }
    }
}