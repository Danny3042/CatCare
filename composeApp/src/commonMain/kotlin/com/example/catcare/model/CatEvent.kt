package com.example.catcare.model

import kotlinx.serialization.Serializable

@Serializable
sealed class CatEvent {
    @Serializable
    data class AddCat(val cat: Cat) : CatEvent()

    @Serializable
    data class RemoveCat(val id: String) : CatEvent()

    @Serializable
    data class Feed(val id: String, val epochMs: Long) : CatEvent()

    @Serializable
    data class UpdateNotes(val id: String, val notes: String) : CatEvent()
}

