package com.example.catcare

actual object AppContextHolder {
    actual var context: Any? = null
    actual fun setAppContext(ctx: Any?) {
        context = null // no-op for iOS in this demo
    }
}
