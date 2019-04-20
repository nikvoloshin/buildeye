package com.github.buildeye.listeners

import com.github.buildeye.collecting.BuildInfoCollector
import com.github.buildeye.collectors.InfrastructureInfoCollector
import com.github.buildeye.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.Action
import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.senders.BuildInfoSender
import com.github.buildeye.utils.createFailureInfo
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildListener : BuildAdapter() {
    private val buildInfoCollector = BuildInfoCollector()

    override fun projectsEvaluated(gradle: Gradle) {
        buildInfoCollector.apply {
            switchesInfo = SwitchesInfoCollector().collect(gradle.startParameter)
            infrastructureInfo = InfrastructureInfoCollector().collect(gradle)
        }

        gradle.taskGraph.addTaskExecutionGraphListener(
                TaskGraphListener(buildInfoCollector.executionInfoCollector)
        )
    }

    override fun buildFinished(result: BuildResult) {
        buildInfoCollector.buildResultInfo = createBuildResultInfo(result)

        val buildInfo = buildInfoCollector.collect()
        BuildInfoSender().send(buildInfo)

        result.rethrowFailure()
    }

    private fun createBuildResultInfo(result: BuildResult): BuildResultInfo {
        val failure = result.failure
        val action = Action.valueOf(result.action.toUpperCase())

        return when (failure) {
            null -> BuildResultInfo(action)
            else -> BuildResultInfo(
                    action,
                    createFailureInfo(failure)
            )
        }
    }
}