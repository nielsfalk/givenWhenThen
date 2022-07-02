plugins {
    kotlin("jvm") version "1.7.0"
    `maven-publish`
    id("com.adarshr.test-logger").version("3.2.0")
}

group = "de.nielsfalk"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.strikt:strikt-core:0.34.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("com.github.nielsfalk:kotlinTabularData:0+")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.16")

    testImplementation("org.springframework.boot:spring-boot-starter:2.7.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.nielsfalk"
            artifactId = "given-when-then"
            version = "1.0"
            from(components["java"])
        }
    }
}
