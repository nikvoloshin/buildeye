package com.github.buildeye.collecting.collectors

import com.github.buildeye.infos.SwitchesInfo
import org.gradle.StartParameter

class SwitchesInfoCollector(private val startParameter: StartParameter) : Collector<SwitchesInfo> {
    override fun collect() = with(startParameter) {
        SwitchesInfo(
                isBuildCacheEnabled,
                isConfigureOnDemand,
                isContinueOnFailure,
                isContinuous,
                isDryRun,
                isOffline,
                isParallelProjectExecutionEnabled,
                isRerunTasks,
                isRefreshDependencies
        )
    }
}