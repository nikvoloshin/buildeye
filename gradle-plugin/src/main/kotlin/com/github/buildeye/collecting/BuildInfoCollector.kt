package com.github.buildeye.collecting

import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.BuildResultInfo
import org.gradle.api.invocation.Gradle

class BuildInfoCollector(gradle: Gradle) : Collector<BuildInfo> {
    private val switchesInfoCollector = SwitchesInfoCollector(gradle.startParameter)
    private val infrastructureInfoCollector = InfrastructureInfoCollector(gradle.startParameter)
    val executionInfoCollector = ExecutionInfoCollector()
    lateinit var buildResultInfo: BuildResultInfo

    override fun collect() = BuildInfo(
            switchesInfoCollector.collect(),
            infrastructureInfoCollector.collect(),
            executionInfoCollector.collect(),
            buildResultInfo
    )
}
