package net.lucypoulton.pronouns.cloud

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class Config(val botToken: String, val channelId: Long, val roleId: Long)

val configPath = Path.of("config.json")

fun main() {
    if (!Files.exists(configPath)) {
        Config::class.java.getResourceAsStream("/config.json").copyTo(FileOutputStream("config.json"))
        println("Default config file created.")
        return
    }
    val config = Json.decodeFromStream<Config>(FileInputStream("config.json"))

    val handler = PronounsListHandler()
    // TODO - extract to config
    Discord(config, handler)
    WebServer(handler).start()
}