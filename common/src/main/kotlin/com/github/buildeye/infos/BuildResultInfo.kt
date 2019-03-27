package com.github.buildeye.infos

sealed class BuildResultInfo
data class BuildFailInfo(val action: String, val failure: Throwable) : BuildResultInfo()
data class BuildSuccessInfo(val action: String) : BuildResultInfo()