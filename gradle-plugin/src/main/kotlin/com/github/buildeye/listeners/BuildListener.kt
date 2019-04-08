package com.github.buildeye.listeners

import com.github.buildeye.collectors.InfrastructureInfoCollector
import com.github.buildeye.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.*
import com.github.buildeye.senders.BuildInfoSender
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildListener(gradle: Gradle) : BuildAdapter() {
    private val switchesInfo: SwitchesInfo = SwitchesInfoCollector().collect(gradle.startParameter)
    private val infrastructureInfo: InfrastructureInfo = InfrastructureInfoCollector().collect(gradle)

    override fun buildFinished(result: BuildResult) {
        val buildInfo = BuildInfo(
                switchesInfo,
                infrastructureInfo,
                createBuildResultInfo(result)
        )
        BuildInfoSender().send(buildInfo)
        result.rethrowFailure()
    }

    private fun createBuildResultInfo(result: BuildResult) = with(result) {
        when (failure) {
            null -> BuildSuccessInfo(action)
            else -> BuildFailInfo(action, failure!!)
        }
    }
}