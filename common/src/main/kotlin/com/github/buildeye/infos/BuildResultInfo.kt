package com.github.buildeye.infos

data class BuildResultInfo(
        val action: String,
        val failure: FailureInfo? = null
)