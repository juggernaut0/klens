plugins {
    kotlin("jvm")
    `klens-publish`
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":klens-api"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")

    val autoServiceVersion = "1.0.1"
    implementation("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    kapt("com.google.auto.service:auto-service:$autoServiceVersion")

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
