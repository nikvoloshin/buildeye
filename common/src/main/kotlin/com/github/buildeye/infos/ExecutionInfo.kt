package com.github.buildeye.infos

import java.util.*

data class ExecutionInfo(
        val executionStartedDate: Date,
        val taskInfos: List<TaskInfo>
)