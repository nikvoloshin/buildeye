package com.github.buildeye.serialization

import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.OutOfDateReason
import com.google.gson.GsonBuilder

object BuildInfoJsonSerializer {
    private val gson = GsonBuilder().apply {
        registerTypeAdapter(OutOfDateReason::class.java, OutOfDateReasonJsonAdapter())
    }.create()

    fun toJson(buildInfo: BuildInfo): String = gson.toJson(buildInfo)

    fun fromJson(json: String): BuildInfo = gson.fromJson(json, BuildInfo::class.java)
}