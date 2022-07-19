package klens.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import klens.processor.KlensSymbolProcessor.Companion.LENS_CLASS_NAME

class LensesGenerator(
    private val logger: KSPLogger,
) {
    fun generateFrom(classDeclaration: KSClassDeclaration): GeneratedFile {
        val className = classDeclaration.simpleName.asString()

        if (!classDeclaration.modifiers.contains(Modifier.DATA)) {
            logger.error("Class $className annotated with @Lenses must be a data class", classDeclaration)
        }

        val name = "${className}Lenses"
        val packageName = classDeclaration.packageName.asString()

        val contents = buildString {
            appendLine("package $packageName")
            appendLine("object $name {")
            for (property in classDeclaration.primaryConstructor!!.parameters) {
                val propName = property.name!!.asString()
                val propType = typeToString(property.type.resolve())
                appendLine("    val $propName: $LENS_CLASS_NAME<$className, $propType> = $LENS_CLASS_NAME({ $propName }, { copy($propName = it) })")
            }
            appendLine("}")
        }

        return GeneratedFile(name = name, packageName = packageName, contents = contents)
    }

    private fun typeToString(type: KSType): String {
        val decl = type.declaration.qualifiedName!!.asString()
        val args = type.arguments
        return if (args.isEmpty()) {
            decl
        } else {
            val argsStr = args.joinToString(separator = ", ", prefix = "<", postfix = ">") {
                typeToString(it.type!!.resolve())
            }
            "$decl$argsStr"
        }
    }
}