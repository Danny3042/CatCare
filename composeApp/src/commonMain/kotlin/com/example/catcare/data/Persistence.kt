package com.example.catcare.data

expect suspend fun saveText(filename: String, text: String)
expect suspend fun loadText(filename: String): String?
