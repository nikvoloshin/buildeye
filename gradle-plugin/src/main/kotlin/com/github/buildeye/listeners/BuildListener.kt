package com.github.buildeye.listeners

import com.github.buildeye.collectors.InfrastructureInfoCollector
import com.github.buildeye.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.*
import com.github.buildeye.senders.BuildInfoSender
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildListener : BuildAdapter() {
    private lateinit var switchesInfo: SwitchesInfo
    private lateinit var infrastructureInfo: InfrastructureInfo

    override fun buildStarted(gradle: Gradle) {
        switchesInfo = SwitchesInfoCollector().collect(gradle.startParameter)
        infrastructureInfo = InfrastructureInfoCollector().collect(gradle)
    }

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