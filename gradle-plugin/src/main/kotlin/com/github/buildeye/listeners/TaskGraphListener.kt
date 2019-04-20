package com.github.buildeye.listeners

import com.github.buildeye.collecting.ExecutionInfoCollector
import com.github.buildeye.listeners.taskExecution.TaskExecutionListenerDecorator
import com.github.buildeye.listeners.taskExecution.addExecutionListener
import com.github.buildeye.utils.millisTime
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import java.util.*

class TaskGraphListener(private val executionInfoCollector: ExecutionInfoCollector) : TaskExecutionGraphListener {
    override fun graphPopulated(graph: TaskExecutionGraph) {
        executionInfoCollector.apply {
            startedDate = Date()
            startedTimestamp = millisTime()
        }

        setTaskExecutionListeners(graph)
    }

    private fun setTaskExecutionListeners(graph: TaskExecutionGraph) {
        graph.apply {
            addTaskExecutionListener(TaskExecutionListenerDecorator())

            allTasks.forEach {
                it.addExecutionListener(BuildEyeTaskExecutionListenerImpl(it, executionInfoCollector.taskInfosCollector))
            }
        }
    }
}