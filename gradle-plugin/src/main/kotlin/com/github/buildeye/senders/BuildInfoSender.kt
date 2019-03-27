package com.github.buildeye.senders

import com.github.buildeye.infos.BuildInfo
import java.io.FileWriter

class BuildInfoSender {
    fun send(buildInfo: BuildInfo) {
        FileWriter("build-result.txt").use {
            it.write(buildInfo.toString())
        }
    }
}