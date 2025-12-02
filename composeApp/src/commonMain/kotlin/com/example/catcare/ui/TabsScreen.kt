package com.example.catcare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catcare.data.CatRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsScreen(
    repository: CatRepository,
    onOpenDetails: (String) -> Unit,
    onFeed: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Tasks", "Breed Classifier", "Settings")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Cat Care") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                }
            }

            when (selectedTab) {
                0 -> TasksScreen()
                1 -> BreedClassifierScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

