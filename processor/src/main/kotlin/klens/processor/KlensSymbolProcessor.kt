package klens.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import klens.Lens
import klens.Lenses

class KlensSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val lensesGenerator: LensesGenerator,
) : SymbolProcessor {
    private var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return emptyList()

        val annotatedClasses = resolver.getSymbolsWithAnnotation(ANNOTATION_NAME)
            .filterIsInstance<KSClassDeclaration>()

        for (classDeclaration in annotatedClasses) {
            lensesGenerator
                .generateFrom(classDeclaration)
                .generate(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }

        invoked = true
        return emptyList()
    }

    @AutoService(SymbolProcessorProvider::class)
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return KlensSymbolProcessor(environment.codeGenerator, LensesGenerator(environment.logger))
        }
    }

    companion object {
        val ANNOTATION_NAME = Lenses::class.qualifiedName!!
        val LENS_CLASS_NAME = Lens::class.qualifiedName!!
    }
}
