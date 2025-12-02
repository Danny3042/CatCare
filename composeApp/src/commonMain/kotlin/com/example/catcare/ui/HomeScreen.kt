package com.example.catcare.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import com.example.catcare.data.CatRepository

@Composable
fun HomeScreen(
    repository: CatRepository,
    onOpenDetails: (String) -> Unit,
    onFeed: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val cats by repository.cats.collectAsState()

    // Content only - App provides topBar and global FAB
    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        itemsIndexed(cats, key = { _, c -> c.id }) { index, cat ->
            // staggered entrance: delay increases with index
            val delay = (index.coerceAtMost(8)) * 60 // cap delay so it's not too long
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(animationSpec = tween(300, delayMillis = delay)) + fadeIn(animationSpec = tween(300, delayMillis = delay)),
                exit = fadeOut(animationSpec = tween(180))
            ) {
                CatRow(cat = cat, onClick = { onOpenDetails(cat.id) }, onFeed = { onFeed(cat.id) })
            }
        }
    }
}
