package com.example.catcare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var notifications by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        RowSetting(label = "Enable notifications", checked = notifications, onChecked = { notifications = it })
    }
}

@Composable
fun RowSetting(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    androidx.compose.foundation.layout.Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}

