package klens.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

class GeneratedFile(val name: String, val packageName: String, val contents: String) {
    fun generate(codeGenerator: CodeGenerator, dependencies: Dependencies) {
        val out = codeGenerator.createNewFile(dependencies = dependencies, packageName = packageName, fileName = name)
        out.writer().use { it.write(contents) }
    }
}
