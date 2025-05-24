package com.example.notes.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.authState.value) {
        if (viewModel.authState.value) {
            onSuccess()
        }
    }

    Column(Modifier.fillMaxSize(), Arrangement.Center) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = { viewModel.login(username, password) }) {
            Text("Войти")
        }
    }
}