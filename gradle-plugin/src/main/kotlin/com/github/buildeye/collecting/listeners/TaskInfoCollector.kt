package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.utils.millisTime
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class TaskInfoCollector(private val executionData: ExecutionData) : TaskExecutionListener {
    override fun beforeExecute(task: Task) {
        executionData.getTaskData(task.path).apply {
            name = task.name
            path = task.path
            startedTimeStamp = millisTime()
        }
    }

    override fun afterExecute(task: Task, state: TaskState) {
        executionData.getTaskData(task.path).finishedTimeStamp = millisTime()
    }
}