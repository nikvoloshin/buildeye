package com.github.buildeye.listeners

import com.github.buildeye.collecting.TaskInfoCollector
import com.github.buildeye.infos.TaskStateInfo
import com.github.buildeye.listeners.taskExecution.BuildEyeTaskExecutionListener
import com.github.buildeye.utils.createFailureInfoOfNullable
import com.github.buildeye.utils.millisTime
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

class BuildEyeTaskExecutionListenerImpl(
        private val task: Task,
        private val taskInfoCollector: TaskInfoCollector
) : BuildEyeTaskExecutionListener {
    override fun beforeExecute() {
        taskInfoCollector.apply {
            setName(task.name)
            setStartedTimestamp(millisTime())
        }
    }

    override fun afterExecute() {
        taskInfoCollector.apply {
            setFinishedTimestamp(millisTime())
            setTaskStateInfo(createTaskStateInfo(task.state))
        }
    }

    private fun createTaskStateInfo(state: TaskState): TaskStateInfo = with(state) {
        return TaskStateInfo(
                executed,
                createFailureInfoOfNullable(failure),
                didWork,
                skipped,
                skipMessage,
                upToDate,
                noSource
        )
    }
}