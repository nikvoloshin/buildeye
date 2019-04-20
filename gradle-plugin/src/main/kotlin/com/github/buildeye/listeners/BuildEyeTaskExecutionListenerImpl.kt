package com.github.buildeye.listeners

import com.github.buildeye.collecting.TaskInfoCollector
import com.github.buildeye.collecting.TaskInfosCollector
import com.github.buildeye.infos.FailureInfo
import com.github.buildeye.infos.TaskStateInfo
import com.github.buildeye.listeners.taskExecution.BuildEyeTaskExecutionListener
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