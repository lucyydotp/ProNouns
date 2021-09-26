import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":pronouns-api"))
    implementation(project(":pronouns-squirtgun"))
    implementation("net.lucypoulton:squirtgun-api:2.0.0-pre6-SNAPSHOT")
    implementation("net.lucypoulton:squirtgun-platform-bungee:2.0.0-pre6-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.bstats:bstats-bungeecord:2.2.1")

    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.9.0")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:22.0.0")
}

tasks {

    withType<Jar> {
        archiveClassifier.set("nodeps")
    }

    val shadowTask = named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")

        minimize {
            exclude(project(":pronouns-api"))
        }

        dependencies {
            exclude(dependency("org.checkerframework:.*:.*"))
            exclude(dependency("com.google..*:.*:.*"))
        }
        // slf4j is transitive from hikari
        relocate("org.slf4j", "me.lucyy.pronouns.deps.slf4j")
        relocate("me.lucyy.squirtgun", "me.lucyy.pronouns.deps.squirtgun")
        relocate("net.kyori", "me.lucyy.pronouns.deps.kyori")
        relocate("org.bstats", "me.lucyy.pronouns.deps.bstats")
        relocate("com.zaxxer.hikari", "me.lucyy.pronouns.deps.hikari")
    }

    named("build") {
        dependsOn(shadowTask)
    }

    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
}

description = "pronouns-bungee"
