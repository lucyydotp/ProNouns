/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(project(":pronouns-api"))
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:RELEASE")
}

description = "pronouns-papi"