package com.example.catcare

import android.content.Context

actual object AppContextHolder {
    actual var context: Any? = null

    actual fun setAppContext(ctx: Any?) {
        context = (ctx as? Context)?.applicationContext
    }
}
