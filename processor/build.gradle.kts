plugins {
    kotlin("jvm")
    `klens-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":klens-api"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.mockk:mockk:1.12.4")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    check {
        // Use example run as a functional test
        dependsOn(":example:run")
    }
}
