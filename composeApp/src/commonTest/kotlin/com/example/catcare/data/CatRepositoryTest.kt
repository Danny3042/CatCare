package com.example.catcare.data

import com.example.catcare.model.Cat
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CatRepositoryTest {
    @Test
    fun testFeedRule() = runBlocking {
        val repo = InMemoryCatRepository()
        val cat = Cat(id = "c1", name = "Mittens", ageMonths = 12)
        repo.add(cat)
        val first = repo.feed("c1")
        assertTrue(first, "First feed should succeed")
        val second = repo.feed("c1")
        assertFalse(second, "Second immediate feed should be prevented")
        val cats = repo.cats
        assertEquals(1, cats.value.size)
    }
}

