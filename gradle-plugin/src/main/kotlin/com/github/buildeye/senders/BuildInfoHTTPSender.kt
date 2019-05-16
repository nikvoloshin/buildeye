package com.github.buildeye.senders

import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.serialization.BuildInfoJsonSerializer
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class BuildInfoHTTPSender(private val serverUrl: Lazy<String?>) : BuildInfoSender {
    override fun send(buildInfo: BuildInfo) {
        serverUrl.value?.also {
            makePostRequest(it, BuildInfoJsonSerializer.toJson(buildInfo))
        }
    }

    private fun makePostRequest(url: String, body: String) {
        (URL(url).openConnection() as HttpURLConnection).apply {
            doOutput = true
            connectTimeout = 5000
            readTimeout = 5000

            setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            outputStream.use {
                it.write(body.toByteArray())
                it.flush()
            }

            try {
                responseCode // actually fires the request
            } catch (e: IOException) {
                return
            }
        }
    }
}