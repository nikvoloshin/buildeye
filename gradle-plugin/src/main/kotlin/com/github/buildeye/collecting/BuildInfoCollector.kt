package com.github.buildeye.collecting

import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.InfrastructureInfo
import com.github.buildeye.infos.SwitchesInfo

class BuildInfoCollector : Collector<BuildInfo> {
    lateinit var switchesInfo: SwitchesInfo
    lateinit var infrastructureInfo: InfrastructureInfo
    val executionInfoCollector = ExecutionInfoCollector()
    lateinit var buildResultInfo: BuildResultInfo

    override fun collect() = BuildInfo(
            switchesInfo,
            infrastructureInfo,
            executionInfoCollector.collect(),
            buildResultInfo
    )
}
