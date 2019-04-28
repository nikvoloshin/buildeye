package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.collectors.InfrastructureInfoCollector
import com.github.buildeye.collecting.collectors.SwitchesInfoCollector
import com.github.buildeye.infos.BuildResultInfo
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildDataCollector(private val buildData: BuildData) : BuildAdapter() {
    override fun projectsEvaluated(gradle: Gradle) {
        buildData.apply {
            switchesInfo = SwitchesInfoCollector(gradle.startParameter).collect()
            infrastructureInfo = InfrastructureInfoCollector(gradle.startParameter).collect()
        }
    }

    override fun buildFinished(result: BuildResult) {
        buildData.buildResultInfo = BuildResultInfo.of(result.action, result.failure)
    }
}