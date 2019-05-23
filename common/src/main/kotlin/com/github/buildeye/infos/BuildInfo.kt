package com.github.buildeye.infos

data class BuildInfo(
        val project: String,
        val switchesInfo: SwitchesInfo,
        val infrastructureInfo: InfrastructureInfo,
        val executionInfo: ExecutionInfo,
        val buildResultInfo: BuildResultInfo
)