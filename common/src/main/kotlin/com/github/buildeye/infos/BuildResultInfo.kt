package com.github.buildeye.infos

data class BuildResultInfo(
        val action: Action,
        val failure: FailureInfo?
) {
    companion object {
        fun of(actionString: String, failure: Throwable?) = BuildResultInfo(
                Action.valueOf(actionString.toUpperCase()),
                FailureInfo.ofNullable(failure)
        )
    }

    enum class Action {
        BUILD, CONFIGURE
    }
}
