package com.github.buildeye

import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.OutOfDateReason
import com.github.buildeye.serialization.OutOfDateReasonJsonAdapter
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticBasePackage
import io.ktor.request.ContentTransformationException
import io.ktor.request.receive
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(OutOfDateReason::class.java, OutOfDateReasonJsonAdapter())
        }
    }
    routing {
        get("/api/builds") {

        }

        post("/api/builds") {
            try {
                val data = call.receive<BuildInfo>()
                println(data.toString())
            } catch (e: ContentTransformationException) {

            }
        }

        static {
            staticBasePackage = "/static"
            resources()
            defaultResource("index.html")
        }

        get("/*") {
            call.respondRedirect("/", true)
        }
    }
}
