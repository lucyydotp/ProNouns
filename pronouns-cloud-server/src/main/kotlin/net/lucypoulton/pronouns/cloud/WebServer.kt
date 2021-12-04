package net.lucypoulton.pronouns.cloud

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.lucypoulton.pronouns.api.set.PronounSet
import java.lang.IllegalArgumentException

@Serializable
data class SubmittedPronounSet(val source: String, val set: String)

class WebServer(val listHandler: PronounsListHandler) {
    fun start() {
        embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                })
            }
            routing {
                get("/") {
                    call.respondText {
                        PronounTier.values()
                            .map(listHandler::getSets)
                            .flatten()
                            .joinToString("\n")
                    }
                }
                post("/api") {
                    val received = call.receive<SubmittedPronounSet>()
                    val parsed: PronounSet = try {
                        PronounSet.parse(received.set)
                    } catch (ex: IllegalArgumentException) {
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond("Badly formatted set")
                        return@post
                    }
                    listHandler.addToQueue(parsed, received.source)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }.start(wait = true)
    }
}