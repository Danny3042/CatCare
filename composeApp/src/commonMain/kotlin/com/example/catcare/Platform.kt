package com.example.catcare

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect object AppContextHolder {
    // on Android this will provide android.content.Context; on other platforms it can be a no-op
    var context: Any?
    fun setAppContext(ctx: Any?)
}
