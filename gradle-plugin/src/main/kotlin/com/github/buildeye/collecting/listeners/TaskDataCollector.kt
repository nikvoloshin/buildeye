package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class TaskDataCollector(private val executionData: ExecutionData) : TaskExecutionListener {
    override fun beforeExecute(task: Task) {
        executionData.getTaskData(task.path).apply {
            path = task.path
            startedOffset = executionData.stopwatch.split()
        }
    }

    override fun afterExecute(task: Task, state: TaskState) {

    }
}