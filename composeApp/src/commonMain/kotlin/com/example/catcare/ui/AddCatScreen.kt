package com.example.catcare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddCatScreen(onSave: (name: String, ageMonths: Int?, notes: String?) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Add Cat", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it },
                label = { Text("Age (months)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    if (name.isBlank()) return@Button
                    val age = ageText.toIntOrNull()
                    onSave(name.trim(), age, notes.trim().ifEmpty { null })
                }, modifier = Modifier.weight(1f)) {
                    Text("Save")
                }

                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
            }
        }
    }
}
