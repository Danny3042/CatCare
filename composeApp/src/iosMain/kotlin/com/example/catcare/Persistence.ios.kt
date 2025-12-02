package com.example.catcare.data

import platform.Foundation.NSHomeDirectory
import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
actual suspend fun saveText(filename: String, text: String) {
    // Write into the app's Documents directory so files survive app restarts
    val dir = NSHomeDirectory()
    val path = "$dir/Documents/$filename"
    memScoped {
        val file = fopen(path, "w")
        if (file != null) {
            fputs(text, file)
            fclose(file)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun loadText(filename: String): String? {
    val dir = NSHomeDirectory()
    val path = "$dir/Documents/$filename"
    return memScoped {
        val file = fopen(path, "r")
        if (file == null) return@memScoped null

        // Seek to end to determine size
        if (fseek(file, 0, SEEK_END) != 0) {
            fclose(file)
            return@memScoped null
        }
        val size = ftell(file)
        if (size <= 0L) {
            fclose(file)
            return@memScoped ""
        }
        rewind(file)

        val intSize = size.toInt()
        val buffer = allocArray<ByteVar>(intSize + 1)
        val rawRead = fread(buffer, 1.convert(), size.convert(), file)
        val read = rawRead.toLong()
        fclose(file)
        if (read <= 0L) return@memScoped null
        buffer[intSize] = 0
        buffer.toKString()
    }
}
