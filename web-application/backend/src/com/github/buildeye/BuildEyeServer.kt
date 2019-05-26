package com.github.buildeye

import com.github.buildeye.Constants.API_BUILDS_PATH
import com.github.buildeye.Constants.API_BUILD_PATH
import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.OutOfDateReason
import com.github.buildeye.serialization.OutOfDateReasonJsonAdapter
import com.github.buildeye.storage.BuildInfosDatabase
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticBasePackage
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

object Env {
    val databaseCatalogue: String by lazy { System.getProperty(Constants.DB_CATALOGUE_PROP, Constants.DEFAULT_DB_CATALOGUE) }
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(OutOfDateReason::class.java, OutOfDateReasonJsonAdapter())
        }
    }

    val storage = BuildInfosDatabase(Env.databaseCatalogue)

    routing {
        get("$API_BUILD_PATH/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let {
                call.respond(storage.fetchBuildInfo(it))
            }
        }

        get(API_BUILDS_PATH) {
            call.respond(storage.fetchAllBuildInfos().sortedByDescending { it.id })
        }

        post(API_BUILDS_PATH) {
            call.receiveOrNull<BuildInfo>()?.let {
                storage.insertBuildInfo(it)
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
