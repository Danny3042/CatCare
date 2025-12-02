package com.example.catcare.ui

import androidx.compose.runtime.Composable

enum class AppIconName { Home, Breed, Settings }

@Composable
expect fun AppIcon(name: AppIconName, contentDescription: String?)
