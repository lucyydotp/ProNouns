import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":pronouns-api"))
    implementation("me.lucyy:squirtgun-api:2.0.0-pre3")
    implementation("me.lucyy:squirtgun-bukkit:2.0.0-pre3")
    implementation("me.lucyy:squirtgun-commands:2.0.0-pre3")
    implementation("com.zaxxer:HikariCP:4.0.2")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.7.0")
    compileOnly("net.kyori:examination-api:1.1.0")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:13.0")
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

        // slf4j is transitive from hikari
        relocate("org.slf4j", "me.lucyy.pronouns.deps.slf4j")
        relocate("me.lucyy.squirtgun", "me.lucyy.pronouns.deps.squirtgun")
        relocate("org.bstats", "me.lucyy.pronouns.deps.bstats")
        relocate("com.zaxxer.hikari", "me.lucyy.pronouns.deps.hikari")
    }

    named("build") {
        dependsOn(shadowTask)
    }
}

description = "pronouns-bukkit"
