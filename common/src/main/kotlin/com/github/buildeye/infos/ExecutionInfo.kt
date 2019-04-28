package com.github.buildeye.infos

data class ExecutionInfo(
        val executionStartedDate: Long,
        val taskInfos: List<TaskInfo>
)