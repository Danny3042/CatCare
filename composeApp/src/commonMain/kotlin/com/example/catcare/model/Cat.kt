package com.example.catcare.model

import kotlinx.serialization.Serializable
import com.example.catcare.data.currentTimeMillis

@Serializable
data class Cat(
    val id: String,
    val name: String,
    val ageMonths: Int? = null,
    val notes: String? = null,
    val lastFedEpochMs: Long? = null
) {
    fun isHungry(currentTimeMs: Long = currentTimeMillis()): Boolean {
        val last = lastFedEpochMs ?: return true
        val oneHourMs = 60 * 60 * 1000
        return currentTimeMs - last >= oneHourMs
    }
}
