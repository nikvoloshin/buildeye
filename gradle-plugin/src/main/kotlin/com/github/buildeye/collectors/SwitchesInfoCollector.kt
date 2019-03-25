package com.github.buildeye.collectors

import com.github.buildeye.SwitchesInfo
import org.gradle.StartParameter

class SwitchesInfoCollector {
    fun collect(startParameter: StartParameter) = with(startParameter) {
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