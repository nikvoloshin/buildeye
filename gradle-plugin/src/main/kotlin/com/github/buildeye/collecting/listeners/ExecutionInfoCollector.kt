package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.utils.millisTime
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import java.util.*

class ExecutionInfoCollector(private val executionData: ExecutionData) : TaskExecutionGraphListener {
    override fun graphPopulated(graph: TaskExecutionGraph) {
        executionData.apply {
            startedDate = Date()
            startedTimeStamp = millisTime()
        }
    }
}