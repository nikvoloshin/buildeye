package com.github.buildeye

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.listeners.*
import com.github.buildeye.inputs.InputsManager
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildEyePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.parent == null) {
            init(target)
        } else {
            target.logger.warn("BuildEye plugin should only be applied in a root project, but applied in: ${target.displayName}")
        }
    }

    private fun init(project: Project) {
        val inputsManager = InputsManager(project)
        val buildData = BuildData()

        project.gradle.addBuildListener(BuildDataCollector(buildData))
        project.gradle.addListener(ExecutionDataCollector(buildData.executionData))
        project.gradle.addListener(TaskDataCollector(buildData.executionData))
        project.gradle.addListener(TaskStateDataCollector(buildData.executionData, inputsManager))
        project.gradle.addListener(BuildCompletionListener(buildData))

        project.logger.info("Initialized BuildEye plugin")
    }
}