package com.github.buildeye.infos

data class TaskInfo(
        val path: String,
        val startedOffset: Long,
        val duration: Long,
        val taskStateInfo: TaskStateInfo
)