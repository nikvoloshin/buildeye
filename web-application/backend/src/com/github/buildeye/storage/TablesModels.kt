package com.github.buildeye.storage

import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.OutOfDateReason

data class BuildInfoTableModel(
        val id: Int,
        val project: String,
        val switchesInfoId: Int,
        val infrastructureInfoId: Int,
        val executionInfoId: Int,
        val buildResultInfoId: Int
)

data class ExecutionInfoTableModel(
        val id: Int,
        val executionStartedDate: Long,
        val duration: Long
)

data class TaskInfoTableModel(
        val id: Int,
        val executionInfoId: Int,
        val path: String,
        val startedOffset: Long,
        val duration: Long,
        val taskStateInfoId: Int
)

data class TaskStateInfoTableModel(
        val id: Int,
        val executed: Boolean,
        val failureId: Int?,
        val didWork: Boolean,
        val skipped: Boolean,
        val skipMessage: String?,
        val upToDate: Boolean,
        val outOfDateReasonId: Int?,
        val noSource: Boolean
)

data class BuildResultInfoTableModel(
        val id: Int,
        val action: BuildResultInfo.Action,
        val failureId: Int?
)

data class OutOfDateReasonTableModel(
        val id: Int,
        val reason: OutOfDateReason.Reason
)
