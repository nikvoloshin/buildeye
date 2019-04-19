package com.github.buildeye.infos

sealed class BuildResultInfo
data class BuildFailInfo(val action: String, val failureMessage: String, val stackTrace: String) : BuildResultInfo()
data class BuildSuccessInfo(val action: String) : BuildResultInfo()