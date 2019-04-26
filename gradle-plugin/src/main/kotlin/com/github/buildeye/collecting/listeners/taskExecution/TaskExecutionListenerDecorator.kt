package com.github.buildeye.collecting.listeners.taskExecution

import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class TaskExecutionListenerDecorator : TaskExecutionListener {
    override fun beforeExecute(task: Task) = task.beforeExecute()
    override fun afterExecute(task: Task, state: TaskState) = task.afterExecute()
}