import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

group = "de.nielsfalk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.strikt:strikt-core:0.34.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
