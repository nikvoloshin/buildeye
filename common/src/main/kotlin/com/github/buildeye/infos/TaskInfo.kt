package com.github.buildeye.infos

import java.time.Duration

data class TaskInfo(
        val name: String,
        val startedTimestamp: Long,
        val finishedTimestamp: Long,
        val taskStateInfo: TaskStateInfo,
        val duration: Duration = Duration.ofMillis(finishedTimestamp - startedTimestamp)
)