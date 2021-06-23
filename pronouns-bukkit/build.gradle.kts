import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(project(":pronouns-api"))
    implementation(project(":pronouns-squirtgun"))
    implementation("me.lucyy:squirtgun-api:2.0.0-pre4")
    implementation("me.lucyy:squirtgun-platform-bukkit:2.0.0-pre4")
    implementation("me.lucyy:squirtgun-commands:2.0.0-pre4")
    implementation("com.zaxxer:HikariCP:4.0.2")
    implementation("org.bstats:bstats-bukkit:2.2.1")

    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.8.1")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:13.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
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

description = "pronouns-bukkit"
