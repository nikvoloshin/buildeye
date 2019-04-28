package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.BuildData
import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.collecting.TaskData
import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.ExecutionInfo
import com.github.buildeye.infos.TaskInfo
import com.github.buildeye.senders.BuildInfoSender
import org.gradle.initialization.BuildCompletionListener

class BuildCompletionListener(private val buildData: BuildData) : BuildCompletionListener {
    @Volatile private var completed = false

    override fun completed() {
        if (!completed) {
            completed = true
            doOnCompleted()
        }
    }

    private fun doOnCompleted() {
        val buildInfo = createBuildInfo(buildData)
        BuildInfoSender().send(buildInfo)
    }

    private fun createBuildInfo(buildData: BuildData) = with(buildData) {
        BuildInfo(
                switchesInfo,
                infrastructureInfo,
                createExecutionInfo(executionData),
                buildResultInfo
        )
    }

    private fun createExecutionInfo(executionData: ExecutionData) = with(executionData) {
        ExecutionInfo(
                startedDate,
                getAllTasksData().map {
                    it.startedTimeStamp -= startedTimeStamp
                    it.finishedTimeStamp -= startedTimeStamp
                    createTaskInfo(it)
                }
        )
    }

    private fun createTaskInfo(taskData: TaskData) = with(taskData) {
        TaskInfo(
                name,
                path,
                startedTimeStamp,
                finishedTimeStamp,
                stateInfo
        )
    }
}