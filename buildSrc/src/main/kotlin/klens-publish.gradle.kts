plugins {
    `maven-publish`
}

group = "com.github.juggernaut0.klens"
val projectVersion: String by rootProject
version = projectVersion

publishing {
    repositories {
        maven {
            name = "pages"
            url = uri("$rootDir/pages/m2/repository")
        }
    }
}
