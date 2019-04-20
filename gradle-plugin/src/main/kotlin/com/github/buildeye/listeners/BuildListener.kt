package com.github.buildeye.listeners

import com.github.buildeye.collecting.BuildInfoCollector
import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.senders.BuildInfoSender
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildListener : BuildAdapter() {
    private lateinit var buildInfoCollector: BuildInfoCollector

    override fun projectsEvaluated(gradle: Gradle) {
        buildInfoCollector = BuildInfoCollector(gradle)

        gradle.taskGraph.addTaskExecutionGraphListener(
                TaskGraphListener(buildInfoCollector.executionInfoCollector)
        )
    }

    override fun buildFinished(result: BuildResult) {
        buildInfoCollector.buildResultInfo = BuildResultInfo.of(result.action, result.failure)

        val buildInfo = buildInfoCollector.collect()
        BuildInfoSender().send(buildInfo)

        result.rethrowFailure()
    }
}