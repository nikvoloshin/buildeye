package com.github.buildeye.infos

data class TaskStateInfo(
        val wasExecuted: Boolean,
        val failure: FailureInfo?,
        val didWork: Boolean,
        val wasSkipped: Boolean,
        val skipMessage: String?,
        val wasUpToDate: Boolean,
        val hadNoSource: Boolean
)