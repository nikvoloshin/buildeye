package com.github.buildeye.infos

data class TaskStateInfo(
        val executed: Boolean,
        val failure: FailureInfo?,
        val didWork: Boolean,
        val skipped: Boolean,
        val skipMessage: String?,
        val upToDate: Boolean,
        val outOfDateReason: OutOfDateReason?,
        val noSource: Boolean
)