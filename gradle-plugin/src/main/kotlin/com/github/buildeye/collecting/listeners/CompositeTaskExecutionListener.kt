package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class CompositeTaskExecutionListener(
        private val executionData: ExecutionData,
        private vararg val listeners : TaskExecutionListener
) : TaskExecutionListener {
    override fun beforeExecute(task: Task) {
        listeners.forEach { it.beforeExecute(task) }

        executionData.getTaskData(task.path).stopwatch.start()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        executionData.getTaskData(task.path).stopwatch.stop()

        listeners.forEach { it.afterExecute(task, state) }
    }
}