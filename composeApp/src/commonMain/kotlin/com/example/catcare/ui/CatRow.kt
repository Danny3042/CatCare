package com.example.catcare.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catcare.model.Cat
import com.example.catcare.data.currentTimeMillis

@Composable
fun CatRow(cat: Cat, onClick: () -> Unit, onFeed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cat.name)
            val ageText = cat.ageMonths?.let { "${it} months" } ?: "Age unknown"
            Text(text = ageText)
            val lastFedText = cat.lastFedEpochMs?.let {
                val mins = (currentTimeMillis() - it) / 60000
                if (mins <= 0) "just now" else "$mins min ago"
            } ?: "never"
            Text(text = "Last fed: $lastFedText")
        }

        Button(onClick = onFeed, enabled = cat.isHungry()) {
            Crossfade(targetState = cat.isHungry()) { hungry ->
                Text(if (hungry) "Feed" else "Fed")
            }
        }
    }
}
