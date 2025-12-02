package com.example.catcare.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
actual fun AppIcon(name: AppIconName, contentDescription: String?) {
    when (name) {
        AppIconName.Home -> Icon(Icons.Filled.Home, contentDescription)
        AppIconName.Breed -> Icon(Icons.Filled.Pets, contentDescription)
        AppIconName.Settings -> Icon(Icons.Filled.Settings, contentDescription)
        AppIconName.Add -> Icon(Icons.Filled.Add, contentDescription)
    }
}
