package com.github.buildeye.listeners

import com.github.buildeye.collecting.BuildInfoCollector
import com.github.buildeye.collectors.InfrastructureInfoCollector
import com.github.buildeye.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.Action
import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value
import com.github.buildeye.senders.BuildInfoSender
import com.github.buildeye.utils.createFailureInfo
import org.apache.log4j.Logger
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildListener : BuildAdapter() {
    companion object {
        private val log = Logger.getLogger(BuildListener::class.java)
    }

    private val buildInfoCollector = BuildInfoCollector()

    override fun projectsEvaluated(gradle: Gradle) {
        buildInfoCollector.apply {
            setSwitchesInfo(SwitchesInfoCollector().collect(gradle.startParameter))
            setInfrastructureInfo(InfrastructureInfoCollector().collect(gradle))
        }

        gradle.taskGraph.addTaskExecutionGraphListener(
                TaskGraphListener(buildInfoCollector.getExecutionInfoCollector())
        )
    }

    override fun buildFinished(result: BuildResult) {
        buildInfoCollector.setBuildResultInfo(createBuildResultInfo(result))

        val buildInfo = buildInfoCollector.get()

        if (buildInfo is Failure) {
            buildInfo.getFailure().forEach(log::error)
        } else {
            BuildInfoSender().send((buildInfo as Value).get())
        }

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