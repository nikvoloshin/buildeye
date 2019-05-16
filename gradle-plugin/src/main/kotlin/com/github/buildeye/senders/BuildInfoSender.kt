package com.github.buildeye.senders

import com.github.buildeye.infos.BuildInfo

interface BuildInfoSender {
    fun send(buildInfo: BuildInfo)
}