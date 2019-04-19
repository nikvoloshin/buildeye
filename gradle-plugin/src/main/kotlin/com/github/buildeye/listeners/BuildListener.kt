package com.github.buildeye.listeners

import com.github.buildeye.collecting.BuildInfoCollector
import com.github.buildeye.collectors.InfrastructureInfoCollector
import com.github.buildeye.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.BuildFailInfo
import com.github.buildeye.infos.BuildSuccessInfo
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value
import com.github.buildeye.senders.BuildInfoSender
import org.apache.log4j.Logger
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle
import org.gradle.internal.impldep.org.apache.commons.lang.exception.ExceptionUtils

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

    private fun createBuildResultInfo(result: BuildResult) = with(result) {
        when (failure) {
            null -> BuildSuccessInfo(action)
            else -> BuildFailInfo(
                    action,
                    failure!!.message ?: "",
                    ExceptionUtils.getStackTrace(failure)
            )
        }
    }
}