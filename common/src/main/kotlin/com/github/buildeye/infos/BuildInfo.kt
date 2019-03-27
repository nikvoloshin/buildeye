package com.github.buildeye.infos

data class BuildInfo(
        val switchesInfo: SwitchesInfo,
        val infrastructureInfo: InfrastructureInfo,
        val buildResultInfo: BuildResultInfo
)