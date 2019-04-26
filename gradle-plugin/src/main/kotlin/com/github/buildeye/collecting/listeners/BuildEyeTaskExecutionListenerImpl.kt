package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.collectors.TaskInfoCollector
import com.github.buildeye.collecting.collectors.TaskInfosCollector
import com.github.buildeye.collecting.listeners.taskExecution.BuildEyeTaskExecutionListener
import com.github.buildeye.infos.FailureInfo
import com.github.buildeye.infos.TaskStateInfo
import com.github.buildeye.utils.millisTime
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

class BuildEyeTaskExecutionListenerImpl(
        private val task: Task,
        private val taskInfosCollector: TaskInfosCollector
) : BuildEyeTaskExecutionListener {
    private val taskInfoCollector = TaskInfoCollector()

    override fun beforeExecute() {
        taskInfoCollector.apply {
            name = task.name
            path = task.path
            startedTimestamp = millisTime()
        }
    }

    override fun afterExecute() {
        taskInfoCollector.apply {
            finishedTimestamp = millisTime()
            taskStateInfo = createTaskStateInfo(task.state)
        }

        taskInfosCollector.addTaskInfo(taskInfoCollector.collect())
    }

    private fun createTaskStateInfo(state: TaskState): TaskStateInfo = with(state) {
        return TaskStateInfo(
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