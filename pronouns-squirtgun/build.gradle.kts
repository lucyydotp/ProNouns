import org.apache.tools.ant.filters.ReplaceTokens

dependencies {
    implementation(project(":pronouns-api"))
    implementation("net.lucypoulton:squirtgun-api:2.0.0-pre9")
    implementation("com.zaxxer:HikariCP:5.0.0")

    implementation("net.kyori:adventure-api:4.9.2")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:22.0.0")
}

tasks.withType<ProcessResources> {
    filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
}

description = "pronouns-squirtgun"
