package com.github.buildeye.infos

data class ExecutionInfo(
        val executionStartedDate: Long,
        val duration: Long,
        val taskInfos: List<TaskInfo>
)