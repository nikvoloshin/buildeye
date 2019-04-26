package com.github.buildeye.infos

data class TaskInfo(
        val name: String,
        val path: String,
        val startedTimestamp: Long,
        val finishedTimestamp: Long,
        val taskStateInfo: TaskStateInfo,
        val duration: Long = finishedTimestamp - startedTimestamp
)