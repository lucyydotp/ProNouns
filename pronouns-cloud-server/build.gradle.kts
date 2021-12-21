import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories.maven("https://m2.dv8tion.net/releases")

val ktorVersion = "1.6.6"

tasks {
    val shadowTask = named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
    }

    named("build") {
        dependsOn(shadowTask)
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.4.0")
    implementation("com.google.guava:guava:31.0.1-jre")

    implementation("ch.qos.logback:logback-classic:1.2.7")

    implementation(project(":pronouns-api"))
    implementation("net.lucypoulton:squirtgun-platform-discord:2.0.0-pre9")
}