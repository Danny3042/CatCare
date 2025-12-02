package com.example.catcare.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import android.content.Context
import com.example.catcare.AppContextHolder

actual suspend fun saveText(filename: String, text: String) {
    withContext(Dispatchers.IO) {
        val ctx = AppContextHolder.context as? Context ?: return@withContext
        val file = File(ctx.filesDir, filename)
        file.writeText(text)
    }
}

actual suspend fun loadText(filename: String): String? {
    return withContext(Dispatchers.IO) {
        val ctx = AppContextHolder.context as? Context ?: return@withContext null
        val file = File(ctx.filesDir, filename)
        if (!file.exists()) return@withContext null
        file.readText()
    }
}
