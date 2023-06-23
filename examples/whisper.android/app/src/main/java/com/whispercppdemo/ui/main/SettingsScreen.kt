package com.whispercppdemo.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("openai_prefs", Context.MODE_PRIVATE)
    val apiKey = remember { mutableStateOf(sharedPreferences.getString("apiKey", "") ?: "") }
    val completionModel = remember { mutableStateOf(sharedPreferences.getString("completionModel", "") ?: "") }
    val maxToken = remember { mutableStateOf(sharedPreferences.getString("maxToken", "") ?: "") }
    val temperature = remember { mutableStateOf(sharedPreferences.getString("temperature", "") ?: "") }
    val completionModels = listOf("davinci", "curie", "babbage", "ada")
    var expanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "OpenAI Settings")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = apiKey.value,
            onValueChange = { apiKey.value = it },
            label = { Text("API Key") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Completion model dropdown
        OutlinedTextField(
            value = completionModel.value,
            onValueChange = { completionModel.value = it },
            label = { Text("Completion Model") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            completionModels.forEach { model ->
                DropdownMenuItem(
                    onClick = {
                        completionModel.value = model
                        expanded = false
                    },
                    text = {Text(model)}
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = maxToken.value,
            onValueChange = { maxToken.value = it },
            label = { Text("Max Token") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = temperature.value,
            onValueChange = { temperature.value = it },
            label = { Text("Temperature") }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            sharedPreferences.edit().apply {
                putString("apiKey", apiKey.value)
                putString("completionModel", completionModel.value)
                putString("maxToken", maxToken.value)
                putString("temperature", temperature.value)
                apply()
            }
            // Save your data...
            scope.launch {
                snackbarHostState.showSnackbar("Data saved successfully")
            }
        }) {
            Text("Save")
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}