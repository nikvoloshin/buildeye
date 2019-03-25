package com.github.buildeye

data class SwitchesInfo(
        val isBuildCacheEnabled: Boolean,
        val isConfigureOnDemand: Boolean,
        val isContinueOnFailure: Boolean,
        val isContinuous: Boolean,
        val isDryRun: Boolean,
        val isOffline: Boolean,
        val isParallelProjectExecutionEnabled: Boolean,
        val isRerunTasks: Boolean,
        val isRefreshDependencies: Boolean
)
