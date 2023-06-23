package com.whispercppdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.whispercppdemo.ui.main.SettingsScreen
import com.whispercppdemo.ui.theme.WhisperCppDemoTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhisperCppDemoTheme {
                SettingsScreen()
            }
        }
    }
}