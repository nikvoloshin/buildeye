package com.github.buildeye

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.listeners.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

class BuildEyePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.parent == null) {
            init(target.gradle)
        } else {
            target.logger.warn("BuildEye plugin should only be applied in a root project, but applied in: ${target.displayName}")
        }
    }

    private fun init(gradle: Gradle) {
        val buildData = BuildData()
        gradle.addBuildListener(BuildDataCollector(buildData))
        gradle.addListener(ExecutionDataCollector(buildData.executionData))
        gradle.addListener(TaskDataCollector(buildData.executionData))
        gradle.addListener(TaskStateDataCollector(buildData.executionData))
        gradle.addListener(BuildCompletionListener(buildData))

        gradle.rootProject.logger.info("Initialized BuildEye plugin")
    }
}