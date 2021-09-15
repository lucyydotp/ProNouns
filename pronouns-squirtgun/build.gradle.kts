import org.apache.tools.ant.filters.ReplaceTokens

dependencies {
    implementation(project(":pronouns-api"))
    implementation("net.lucypoulton:squirtgun-api:2.0.0-pre6-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:4.0.2")

    implementation("net.kyori:adventure-api:4.8.1")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:13.0")
}

tasks.withType<ProcessResources> {
    filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
}

description = "pronouns-squirtgun"
