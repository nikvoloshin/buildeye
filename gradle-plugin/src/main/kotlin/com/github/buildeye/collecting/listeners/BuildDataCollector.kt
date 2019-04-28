package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.Collectors
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.invocation.Gradle

class BuildDataCollector(private val buildData: BuildData) : BuildAdapter() {
    override fun projectsEvaluated(gradle: Gradle) {
        buildData.apply {
            switchesInfo = Collectors.switchesInfo(gradle.startParameter)
            infrastructureInfo = Collectors.infrastructureInfo(gradle.startParameter)
        }
    }

    override fun buildFinished(result: BuildResult) {
        buildData.buildResultInfo = Collectors.buildResultInfo(result)
    }
}