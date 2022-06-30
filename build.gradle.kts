plugins {
    kotlin("jvm") version "1.6.20"
    application
    `maven-publish`
}

group = "de.nielsfalk"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.strikt:strikt-core:0.34.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.16")

    testImplementation("org.springframework.boot:spring-boot-starter:2.7.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.nielsfalk"
            artifactId = "color-console"
            version = "1.0"

            from(components["java"])
        }
    }
}