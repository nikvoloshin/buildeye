package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.Collectors
import com.github.buildeye.collecting.ExecutionData
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class TaskStateDataCollector(private val executionData: ExecutionData) : TaskExecutionListener {
    override fun beforeExecute(task: Task) {

    }

    override fun afterExecute(task: Task, state: TaskState) {
        executionData.getTaskData(task.path).stateInfo = Collectors.taskStateInfo(state)
    }
}