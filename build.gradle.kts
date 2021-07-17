
plugins {
    `maven-publish`
    java
    signing
}

subprojects {
    version = "1.3.2"
    group = "me.lucyy"

    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
    apply<JavaPlugin>()

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
    }

    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        mavenCentral()
    }

    dependencies {
        implementation("me.lucyy:squirtgun-api:2.0.0-pre5")
    }
}