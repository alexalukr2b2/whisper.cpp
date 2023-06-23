package com.whispercppdemo.ui.main

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.whispercppdemo.R


@Composable
fun MainScreen(viewModel: MainScreenViewModel, navController: NavHostController) {
    MainScreen(
        canTranscribe = viewModel.canTranscribe,
        canSummarize = viewModel.canSummarize,
        isRecording = viewModel.isRecording,
        messageLog = viewModel.dataLog,
        onBenchmarkTapped = viewModel::benchmark,
        onTranscribeSampleTapped = viewModel::transcribeSample,
        onRecordTapped = viewModel::toggleRecord,
        onSummarizeTapped = viewModel::sendTranscribedTextToChatGpt,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    canTranscribe: Boolean,
    canSummarize: Boolean,
    isRecording: Boolean,
    messageLog: String,
    onBenchmarkTapped: () -> Unit,
    onTranscribeSampleTapped: () -> Unit,
    onRecordTapped: () -> Unit,
    onSummarizeTapped: () -> Unit,
    navController: NavController,
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(


        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { dropdownMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Open overflow menu"
                        )
                    }
                    DropdownMenu(
                        expanded = dropdownMenuExpanded,
                        onDismissRequest = { dropdownMenuExpanded = false }
                    ) {
                        // Add menu items here
                        DropdownMenuItem(
                            text = { Text("Settings")  },
                            onClick = {
                                dropdownMenuExpanded = false
                                navController.navigate("settings")
                            })

                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    BenchmarkButton(enabled = canTranscribe, onClick = onBenchmarkTapped)
                    TranscribeSampleButton(enabled = canTranscribe, onClick = onTranscribeSampleTapped)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                    RecordButton(enabled = canTranscribe, isRecording = isRecording, onClick = onRecordTapped)
                    SummarizeButton(enabled = canSummarize, onClick = onSummarizeTapped)
                }

            }
            MessageLog(messageLog)
        }
    }
}

@Composable
private fun MessageLog(log: String) {
    SelectionContainer() {
        Text(modifier = Modifier.verticalScroll(rememberScrollState()), text = log)
    }
}

@Composable
private fun BenchmarkButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled) {
        Text("Benchmark")
    }
}

@Composable
private fun TranscribeSampleButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled) {
        Text("Transcribe sample")
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SummarizeButton(enabled: Boolean, onClick: () -> Unit) {
    val internetPermissionState = rememberPermissionState(
        permission = Manifest.permission.INTERNET,
        onPermissionResult = {granted ->
            if (granted) {
                onClick()
            }
        })
    Button(onClick = {
        if (internetPermissionState.status.isGranted) {
            onClick()
        } else {
            internetPermissionState.launchPermissionRequest()
        }
    }, enabled = enabled) {
        Text("Summarize")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RecordButton(enabled: Boolean, isRecording: Boolean, onClick: () -> Unit) {
    val micPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.RECORD_AUDIO,
        onPermissionResult = { granted ->
            if (granted) {
                onClick()
            }
        }
    )
    Button(onClick = {
        if (micPermissionState.status.isGranted) {
            onClick()
        } else {
            micPermissionState.launchPermissionRequest()
        }
     }, enabled = enabled) {
        Text(
            if (isRecording) {
                "Stop recording"
            } else {
                "Start recording"
            }
        )
    }
}