package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.infos.FailureInfo
import com.github.buildeye.infos.TaskStateInfo
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class TaskStateCollector(private val executionData: ExecutionData) : TaskExecutionListener {
    override fun beforeExecute(task: Task) {

    }

    override fun afterExecute(task: Task, state: TaskState) {
        executionData.getTaskData(task.path).stateInfo = createStateInfo(state)
    }

    private fun createStateInfo(state: TaskState) = with(state) {
        TaskStateInfo(
                executed,
                FailureInfo.ofNullable(failure),
                didWork,
                skipped,
                skipMessage,
                upToDate,
                noSource
        )
    }
}