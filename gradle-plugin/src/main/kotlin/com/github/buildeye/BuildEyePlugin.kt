package com.github.buildeye

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.listeners.*
import com.github.buildeye.inputs.InputsManager
import com.github.buildeye.senders.BuildInfoHTTPSender
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildEyePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("buildeye", BuildEyePluginExtension::class.java)

        if (target.parent == null) {
            init(target, extension)
        } else {
            target.logger.warn("BuildEye plugin should only be applied in a root project, but applied in: ${target.displayName}")
        }
    }

    private fun init(project: Project, extension: BuildEyePluginExtension) {
        val inputsManager = InputsManager(project)
        val buildData = BuildData(project.name)
        val buildInfoSender = BuildInfoHTTPSender(lazy(extension::serverUrl))

        listOf(
                BuildDataCollector(buildData),
                ExecutionDataCollector(buildData.executionData),
                CompositeTaskExecutionListener(
                        buildData.executionData,
                        TaskDataCollector(buildData.executionData),
                        TaskStateDataCollector(buildData.executionData, inputsManager)
                ),
                BuildCompletionListener(buildData, buildInfoSender)
        ).forEach(project.gradle::addListener)

        project.logger.info("Initialized BuildEye plugin")
    }
}