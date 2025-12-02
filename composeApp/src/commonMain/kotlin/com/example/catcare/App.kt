package com.example.catcare

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.scale
import com.example.catcare.data.CatRepository
import com.example.catcare.data.InMemoryCatRepository
import com.example.catcare.data.loadText
import com.example.catcare.data.saveText
import com.example.catcare.model.Cat
import com.example.catcare.ui.AddCatScreen
import com.example.catcare.ui.CatDetailsScreen
import com.example.catcare.ui.BreedClassifierScreen
import com.example.catcare.ui.SettingsScreen
import com.example.catcare.ui.HomeScreen
import com.example.catcare.ui.AppIcon
import com.example.catcare.ui.AppIconName
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class Details(val id: String) : Screen()
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val repository: CatRepository = remember { InMemoryCatRepository() }
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Home, 1: Breed, 2: Settings
    var showAddSheet by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load onboarding flag once on first composition
    LaunchedEffect(Unit) {
        val flag = loadText("onboarding_shown")
        if (flag != "true") {
            showOnboarding = true
        }
    }

    fun navigate(to: Screen) { screen = to }
    fun selectTab(index: Int) { selectedTab = index }
    fun openAdd() { showAddSheet = true }
    fun closeAdd() { showAddSheet = false }

    MaterialTheme {
        Scaffold(
            topBar = { CenterAlignedTopAppBar(title = { Text("Cat Care") }) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectTab(0) },
                        icon = { NavIcon(name = AppIconName.Home, selected = selectedTab == 0, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectTab(1) },
                        icon = { NavIcon(name = AppIconName.Breed, selected = selectedTab == 1, contentDescription = "Breed") },
                        label = { Text("Breed") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectTab(2) },
                        icon = { NavIcon(name = AppIconName.Settings, selected = selectedTab == 2, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { openAdd() }) {
                    AppIcon(AppIconName.Add, "Add cat")
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            // Main content area
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {

                when (val s = screen) {
                    is Screen.Details -> CatDetailsScreen(
                        catId = s.id,
                        repository = repository,
                        onBack = { navigate(Screen.Home) },
                        onFeed = { id -> scope.launch { repository.feed(id) } },
                        onSaveNotes = { id, notes -> scope.launch { repository.updateNotes(id, notes) } }
                    )

                    else -> {
                        // Show selected tab content with a small animated crossfade
                        AnimatedContent(targetState = selectedTab, transitionSpec = { fadeIn(tween(220)).togetherWith(fadeOut(tween(180))) }) { tab ->
                            when (tab) {
                                0 -> HomeScreen(
                                    repository = repository,
                                    onOpenDetails = { id -> navigate(Screen.Details(id)) },
                                    onFeed = { id ->
                                        scope.launch {
                                            val ok = repository.feed(id)
                                            if (ok) snackbarHostState.showSnackbar("Fed") else snackbarHostState.showSnackbar("Can't feed yet")
                                        }
                                    },
                                    snackbarHostState = snackbarHostState
                                )
                                1 -> BreedClassifierScreen()
                                2 -> SettingsScreen()
                                else -> HomeScreen(
                                    repository = repository,
                                    onOpenDetails = { id -> navigate(Screen.Details(id)) },
                                    onFeed = { id ->
                                        scope.launch {
                                            val ok = repository.feed(id)
                                            if (ok) snackbarHostState.showSnackbar("Fed") else snackbarHostState.showSnackbar("Can't feed yet")
                                        }
                                    },
                                    snackbarHostState = snackbarHostState
                                )
                            }
                        }
                    }
                }

                // Modal bottom sheet for Add
                if (showAddSheet) {
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ModalBottomSheet(onDismissRequest = { closeAdd() }, sheetState = sheetState) {
                        AddCatScreen(onSave = { name, ageMonths, notes ->
                            scope.launch {
                                repository.add(Cat(id = "", name = name, ageMonths = ageMonths, notes = notes))
                                closeAdd()
                            }
                        }, onCancel = { closeAdd() })
                    }
                }

                // Onboarding overlay (one-time)
                if (showOnboarding) {
                    OnboardingOverlay(onDismiss = {
                        scope.launch {
                            saveText("onboarding_shown", "true")
                            showOnboarding = false
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun NavIcon(name: AppIconName, selected: Boolean, contentDescription: String?) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.15f else 1.0f, animationSpec = tween(220))
    Box(modifier = Modifier.scale(scale)) {
        AppIcon(name, contentDescription)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingOverlay(onDismiss: () -> Unit) {
    // full-screen scrim with a centered card
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0x80000000)), contentAlignment = Alignment.Center) {

        AnimatedVisibility(visible = true, enter = fadeIn(tween(220)), exit = fadeOut(tween(180))) {
            Surface(shape = RoundedCornerShape(12.dp), tonalElevation = 8.dp, modifier = Modifier.padding(24.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome to Cat Care", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Use the tabs below to switch between Home, Breed Classifier and Settings.\nTap the + button to add a cat.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onDismiss) {
                        Text("Got it")
                    }
                }
            }
        }
    }
}
