import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

tasks {
    withType<Jar> {
        archiveClassifier.set("nodeps")
    }

    val shadow = withType<ShadowJar> {
        archiveClassifier.set("")
    }

    named("build") {
        dependsOn(shadow)
    }
    jar {
        manifest {
            attributes(
                "Main-Class" to "me.lucyy.pronouns.discord.ProNounsDiscordStandalone"
            )
        }
    }
}


repositories.maven("https://m2.dv8tion.net/releases")

dependencies {
    implementation(project(":pronouns-api"))
    implementation(project(":pronouns-squirtgun"))
    implementation("net.lucypoulton:squirtgun-platform-discord:2.0.0-pre6-SNAPSHOT")
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("mysql:mysql-connector-java:8.0.26")
}
