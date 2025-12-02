package com.example.catcare.data

import com.example.catcare.model.Cat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.random.Random

interface CatRepository {
    val cats: StateFlow<List<Cat>>
    suspend fun add(cat: Cat)
    /**
     * Attempts to feed a cat. Returns true if fed, false if business rule prevented feeding.
     */
    suspend fun feed(id: String): Boolean
    suspend fun updateNotes(id: String, notes: String): Boolean
    suspend fun load()
    suspend fun save()
}

class InMemoryCatRepository(private val autoSave: Boolean = true) : CatRepository {
    private val mutex = Mutex()
    private val _cats = MutableStateFlow<List<Cat>>(emptyList())
    override val cats: StateFlow<List<Cat>> = _cats

    private val scope = CoroutineScope(Dispatchers.Default)
    private val storageFile = "cats.json"

    init {
        if (autoSave) {
            scope.launch {
                load()
                if (_cats.value.isEmpty()) {
                    addDemoData()
                    save()
                }
            }
        }
    }

    override suspend fun load() {
        mutex.withLock {
            val text = loadText(storageFile) ?: return
            try {
                val list = Json.decodeFromString<List<Cat>>(text)
                _cats.value = list
            } catch (_: Exception) {
                // ignore parse errors
            }
        }
    }

    override suspend fun save() {
        mutex.withLock {
            try {
                val text = Json.encodeToString(_cats.value)
                saveText(storageFile, text)
            } catch (_: Exception) {
                // ignore save errors for now
            }
        }
    }

    override suspend fun add(cat: Cat) {
        mutex.withLock {
            val newCat = if (cat.id.isBlank()) cat.copy(id = randomId()) else cat
            _cats.value = _cats.value + newCat
            if (autoSave) save()
        }
    }

    override suspend fun feed(id: String): Boolean {
        mutex.withLock {
            val idx = _cats.value.indexOfFirst { it.id == id }
            if (idx == -1) return false
            val now = currentTimeMillis()
            val cat = _cats.value[idx]
            if (!cat.isHungry(now)) return false
            val updated = cat.copy(lastFedEpochMs = now)
            val list = _cats.value.toMutableList()
            list[idx] = updated
            _cats.value = list
            if (autoSave) save()
            return true
        }
    }

    override suspend fun updateNotes(id: String, notes: String): Boolean {
        mutex.withLock {
            val idx = _cats.value.indexOfFirst { it.id == id }
            if (idx == -1) return false
            val cat = _cats.value[idx]
            val updated = cat.copy(notes = notes)
            val list = _cats.value.toMutableList()
            list[idx] = updated
            _cats.value = list
            if (autoSave) save()
            return true
        }
    }

    private fun addDemoData() {
        val now = currentTimeMillis()
        _cats.value = listOf(
            Cat(id = randomId(), name = "Mittens", ageMonths = 24, notes = "Loves naps", lastFedEpochMs = now - 2 * 60 * 60 * 1000),
            Cat(id = randomId(), name = "Shadow", ageMonths = 12, notes = "Playful at night", lastFedEpochMs = null),
            Cat(id = randomId(), name = "Luna", ageMonths = 6, notes = "Needs brushing", lastFedEpochMs = now - 30 * 60 * 1000)
        )
    }

    private fun randomId(): String = Random.nextLong().toString()
}
