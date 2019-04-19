package com.github.buildeye.collecting

import com.github.buildeye.collecting.property.InfoCollector
import com.github.buildeye.infos.TaskInfo
import com.github.buildeye.property.EitherValueFailure
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value

class TaskInfosCollector : InfoCollector<List<TaskInfo>>("task infos") {
    private val taskInfoCollectors = mutableListOf<TaskInfoCollector>()

    override fun get(): EitherValueFailure<List<TaskInfo>, List<String>> {
        val (collected, withFailure) = taskInfoCollectors.map(TaskInfoCollector::get).partition { it is Value }
        val failures = withFailure.map { (it as Failure).getFailure() }.flatten()

        return if (failures.isNotEmpty()) {
            Failure(failures)
        } else {
            Value(collected.map { (it as Value).get() })
        }
    }

    fun addTaskInfoCollector(taskInfoCollector: TaskInfoCollector) = taskInfoCollectors.add(taskInfoCollector)
}