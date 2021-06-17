dependencies {
    implementation(project(":pronouns-api"))
    implementation("me.lucyy:squirtgun-api:2.0.0-pre4")
    implementation("me.lucyy:squirtgun-commands:2.0.0-pre4")
    implementation("com.zaxxer:HikariCP:4.0.2")

    implementation("net.kyori:adventure-api:4.8.1")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:13.0")
}

description = "pronouns-squirtgun"
