package com.example.catcare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catcare.data.CatRepository

@Composable
fun CatDetailsScreen(catId: String, repository: CatRepository, onBack: () -> Unit, onFeed: (String) -> Unit, onSaveNotes: (String, String) -> Unit) {
    val cats by repository.cats.collectAsState()
    val cat = cats.firstOrNull { it.id == catId }
    if (cat == null) {
        Text("Cat not found")
        return
    }
    var notes by remember { mutableStateOf(cat.notes ?: "") }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Text(text = "Name: ${cat.name}")
            Text(text = "Age: ${cat.ageMonths ?: "unknown"}")
            Text(text = "Last fed: ${cat.lastFedEpochMs ?: "never"}")
            OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { onSaveNotes(catId, notes) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("Save notes")
            }
            Button(onClick = { onFeed(catId) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(if (cat.isHungry()) "Feed" else "Fed")
            }
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("Back")
            }
        }
    }
}
