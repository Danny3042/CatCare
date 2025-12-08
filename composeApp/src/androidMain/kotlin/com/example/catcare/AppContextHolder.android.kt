package com.example.catcare

import android.content.Context

actual object AppContextHolder {
    actual var context: Any? = null

    // set the context via the property to avoid JVM signature clash
}
