package com.example.catcare.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
actual fun AppIcon(name: AppIconName, contentDescription: String?) {
    when (name) {
        AppIconName.Home -> Text("🏠")
        AppIconName.Breed -> Text("🐾")
        AppIconName.Settings -> Text("⚙️")
        AppIconName.Add -> Text("＋")
    }
}
