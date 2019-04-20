package com.github.buildeye.infos

data class BuildResultInfo(
        val action: Action,
        val failure: FailureInfo? = null
)

enum class Action {
    BUILD, CONFIGURE
}