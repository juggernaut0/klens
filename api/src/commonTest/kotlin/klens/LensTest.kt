package klens

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class LensTest {
    @Test
    fun constructorCreatesAWorkingLens() {
        val lens = Lens<String, String>(
            getter = {
                assertEquals("this", this)
                "getter"
            },
            setter = {
                assertEquals("this", this)
                assertEquals("it", it)
                "setter"
            },
        )

        assertEquals("getter", lens.get("this"))
        assertEquals("setter", lens.set("this", "it"))
    }

    @Test
    fun composingLensesCreatesAWorkingLensGetter() {
        val lens1 = Lens<String, String>(
            getter = {
                assertEquals("this", this)
                "getter1"
            },
            setter = { fail() },
        )
        val lens2 = Lens<String, String>(
            getter = {
                assertEquals("getter1", this)
                "getter2"
            },
            setter = { fail() },
        )

        val lens3 = lens1 * lens2

        assertEquals("getter2", lens3.get("this"))
    }

    @Test
    fun composingLensesCreatesAWorkingLensSetter() {
        val lens1 = Lens<String, String>(
            getter = {
                assertEquals("this", this)
                "getter1"
            },
            setter = {
                assertEquals("this", this)
                assertEquals("setter2", it)
                "setter1"
            },
        )
        val lens2 = Lens<String, String>(
            getter = { fail() },
            setter = {
                assertEquals("getter1", this)
                assertEquals("it", it)
                "setter2"
            },
        )

        val lens3 = lens1 * lens2

        assertEquals("setter1", lens3.set("this", "it"))
    }

    @Test
    fun firstItemLensTest() {
        val firstItemLens = Lens<List<Int>, Int>(
            getter = { this[0] },
            setter = { listOf(it) + subList(1, size) }
        )

        assertEquals(42, firstItemLens.get(listOf(42, 0, 1337)))
        assertEquals(listOf(42, 0, 1337), firstItemLens.set(listOf(0, 0, 1337), 42))
    }
}