package com.example.notes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.ui.auth.AuthScreen
import com.example.notes.ui.auth.AuthViewModel
import com.example.notes.ui.notes.NotesScreen

@Composable
fun MainApp() {
    val authViewModel: AuthViewModel = viewModel()

    if (authViewModel.authState.value) {
        NotesScreen()
    } else {
        AuthScreen(onSuccess = { })
    }
}