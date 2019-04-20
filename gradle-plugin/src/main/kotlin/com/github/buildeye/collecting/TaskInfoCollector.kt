package com.github.buildeye.collecting

import com.github.buildeye.collecting.property.InfoCollector
import com.github.buildeye.collecting.property.InfoProvider
import com.github.buildeye.infos.TaskInfo
import com.github.buildeye.infos.TaskStateInfo
import com.github.buildeye.property.EitherValueFailure
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value

class TaskInfoCollector(taskName: String) : InfoCollector<TaskInfo>("$taskName task info") {
    private val nameProvider = InfoProvider<String>("$taskName task name")
    private val startedTimestampProvider = InfoProvider<Long>("$taskName execution started timestamp")
    private val finishedTimestampProvider = InfoProvider<Long>("$taskName execution finished timestamp")
    private val taskStateInfoProvider = InfoProvider<TaskStateInfo>("$taskName task state info")

    override fun get(): EitherValueFailure<TaskInfo, List<String>> {
        val name = nameProvider.get()
        val startedTimestamp = startedTimestampProvider.get()
        val finishedTimestamp = finishedTimestampProvider.get()
        val taskStateInfo = taskStateInfoProvider.get()

        val failures = mutableListOf<String>()
        if (name is Failure) {
            failures.add(name.getFailure())
        }
        if (startedTimestamp is Failure) {
            failures.add(startedTimestamp.getFailure())
        }
        if (finishedTimestamp is Failure) {
            failures.add(finishedTimestamp.getFailure())
        }
        if (taskStateInfo is Failure) {
            failures.add(taskStateInfo.getFailure())
        }

        return if (failures.isNotEmpty()) {
            Failure(failures)
        } else {
            Value(
                    TaskInfo(
                            (name as Value).get(),
                            (startedTimestamp as Value).get(),
                            (finishedTimestamp as Value).get(),
                            (taskStateInfo as Value).get()
                    )
            )
        }
    }

    fun setName(name: String) = nameProvider.set(name)

    fun setStartedTimestamp(startedTimestamp: Long) = startedTimestampProvider.set(startedTimestamp)

    fun setFinishedTimestamp(finishedTimestamp: Long) = finishedTimestampProvider.set(finishedTimestamp)

    fun setTaskStateInfo(taskStateInfo: TaskStateInfo) = taskStateInfoProvider.set(taskStateInfo)
}