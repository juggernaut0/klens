plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":klens-api"))
    ksp(project(":klens-processor"))
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
    }
}

application {
    mainClass.set("klens.example.MainKt")
}
