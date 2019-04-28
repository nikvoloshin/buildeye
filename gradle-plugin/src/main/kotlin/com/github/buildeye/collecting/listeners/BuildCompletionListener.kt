package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.Collectors
import com.github.buildeye.senders.BuildInfoSender
import org.gradle.initialization.BuildCompletionListener

class BuildCompletionListener(private val buildData: BuildData) : BuildCompletionListener {
    @Volatile private var completed = false

    override fun completed() {
        if (!completed) {
            completed = true
            doOnCompleted()
        }
    }

    private fun doOnCompleted() {
        val buildInfo = Collectors.buildInfo(buildData)
        BuildInfoSender().send(buildInfo)
    }
}