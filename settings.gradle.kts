pluginManagement {
    plugins {
        kotlin("jvm") version "1.7.10"
        kotlin("multiplatform") version "1.7.10"
    }
}

include("api", "example", "processor")

rootProject.name = "klens"
project(":api").name = "klens-api"
project(":processor").name = "klens-processor"
