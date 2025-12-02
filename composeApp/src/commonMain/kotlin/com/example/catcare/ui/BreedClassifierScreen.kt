package com.example.catcare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun BreedClassifierScreen() {
    var result by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Breed Classifier (demo)", style = MaterialTheme.typography.titleMedium)

        // portable placeholder for an image picker / preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFFEFEFEF)),
            contentAlignment = Alignment.Center
        ) {
            Text("Image placeholder")
        }

        Button(onClick = { result = demoClassify() }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Classify image")
        }
        result?.let { Text("Result: $it", modifier = Modifier.padding(top = 12.dp)) }
    }
}

fun demoClassify(): String {
    val breeds = listOf("Siamese", "Maine Coon", "Persian", "Bengal", "Domestic Shorthair")
    return breeds[Random.nextInt(breeds.size)]
}
