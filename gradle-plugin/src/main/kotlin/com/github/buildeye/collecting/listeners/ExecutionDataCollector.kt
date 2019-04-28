package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

class ExecutionDataCollector(private val executionData: ExecutionData) : TaskExecutionGraphListener {
    override fun graphPopulated(graph: TaskExecutionGraph) {
        executionData.apply {
            startedDate = System.currentTimeMillis()
            stopwatch.start()
        }
    }
}