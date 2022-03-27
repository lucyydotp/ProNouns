
plugins {
    `maven-publish`
    java
    signing
}

subprojects {
    version = "2.0.4"
    group = "net.lucypoulton"

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
        mavenLocal()
    }

    dependencies {
        implementation("net.lucypoulton:squirtgun-api:2.0.0-pre9-patch.1")
    }
}