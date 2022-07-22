package klens.processor

import com.google.devtools.ksp.processing.CodeGenerator
import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class GeneratedFileTest {
    @Test
    fun `generate write file`() {
        val os = ByteArrayOutputStream()
        val codeGenerator = mockk<CodeGenerator> {
            every { createNewFile(any(), "package", "name") } returns os
        }
        val generatedFile = GeneratedFile("name", "package", "contents")

        generatedFile.generate(codeGenerator, mockk())

        assertEquals("contents", os.toString())
    }
}