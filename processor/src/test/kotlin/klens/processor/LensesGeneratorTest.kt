package klens.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class LensesGeneratorTest {
    @Test
    fun `generates from classDeclaration`() {
        val lensesGenerator = LensesGenerator(mockk(relaxUnitFun = true))
        val classDeclaration = mockk<KSClassDeclaration> {
            every { simpleName } returns ksName("Foo")
            every { modifiers } returns setOf(Modifier.DATA)
            every { packageName } returns ksName("com.example")
            every { primaryConstructor } returns mockk {
                every { parameters } returns listOf(mockk {
                    every { name } returns ksName("bar")
                    every { type } returns ksType(ksName("bar.Bar"), emptyList())
                })
            }
        }

        val generatedFile = lensesGenerator.generateFrom(classDeclaration)

        assertEquals("FooLenses", generatedFile.name)
        assertEquals("com.example", generatedFile.packageName)
        val expectedContents = """
            package com.example
            object FooLenses {
                val bar: klens.Lens<Foo, bar.Bar> = klens.Lens({ bar }, { copy(bar = it) })
            }
            
        """.trimIndent()
        assertEquals(expectedContents, generatedFile.contents)
    }

    @Test
    fun `errors on not data class`() {
        val logger = mockk<KSPLogger>(relaxUnitFun = true)
        val lensesGenerator = LensesGenerator(logger)
        val classDeclaration = mockk<KSClassDeclaration> {
            every { simpleName } returns ksName("Foo")
            every { modifiers } returns emptySet()
            every { packageName } returns ksName("com.example")
            every { primaryConstructor } returns mockk {
                every { parameters } returns emptyList()
            }
        }

        lensesGenerator.generateFrom(classDeclaration)

        verify { logger.error(any(), eq(classDeclaration)) }
    }

    @Test
    fun `works with generic types`() {
        val lensesGenerator = LensesGenerator(mockk(relaxUnitFun = true))
        val classDeclaration = mockk<KSClassDeclaration> {
            every { simpleName } returns ksName("Foo")
            every { modifiers } returns setOf(Modifier.DATA)
            every { packageName } returns ksName("com.example")
            every { primaryConstructor } returns mockk {
                every { parameters } returns listOf(mockk {
                    every { name } returns ksName("bar")
                    every { type } returns ksType(ksName("kotlin.Map"), listOf(
                        ksType(ksName("bar.Bar"), emptyList()),
                        ksType(ksName("kotlin.Int"), emptyList()),
                    ))
                })
            }
        }

        val generatedFile = lensesGenerator.generateFrom(classDeclaration)

        val expectedContents = """
            package com.example
            object FooLenses {
                val bar: klens.Lens<Foo, kotlin.Map<bar.Bar, kotlin.Int>> = klens.Lens({ bar }, { copy(bar = it) })
            }
            
        """.trimIndent()
        assertEquals(expectedContents, generatedFile.contents)
    }

    private fun ksName(name: String): KSName {
        return object : KSName {
            override fun asString(): String = name
            override fun getQualifier(): String = name.split('.').let { it.subList(0, it.size-1) }.joinToString(separator = ".")
            override fun getShortName(): String = name.split('.').last()
        }
    }

    private fun ksType(name: KSName, args: List<KSTypeReference>): KSTypeReference {
        return mockk {
            every { resolve() } returns mockk {
                every { declaration } returns mockk {
                    every { qualifiedName } returns name
                }
                every { arguments } returns args.map {
                    mockk {
                        every { type } returns it
                    }
                }
            }
        }
    }
}