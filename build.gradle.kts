import kotlin.text.Regex

plugins {
    kotlin("jvm") version "1.7.22"
    `java-library`
    `maven-publish`
}

group = "uks.master.thesis"
version = "0.3.4"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.19.0")
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    testImplementation(kotlin("test"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        ))
    }
}

tasks.build {
    dependsOn("updateVersionInREADME")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.register("updateVersionInREADME") {
    val readme = File(rootDir.absolutePath + "/README.md")
    val content = readme.readText()
    val updatedContent = content
        .replace(Regex("<version>([0-9\\.]+)</version>"), "<version>${version}</version>")
        .replace(Regex("com\\.github\\.masterchi3f:master-iac\\:([0-9\\.]+)"), "com.github.masterchi3f:master-iac:${version}")
        .replace(Regex("master-iac\\/([0-9\\.]+)"), "master-iac/${version}")
    readme.writeText(updatedContent)
}
