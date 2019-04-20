package com.github.buildeye.collecting

import com.github.buildeye.infos.TaskInfo

class TaskInfosCollector : Collector<List<TaskInfo>> {
    private val taskInfoCollectors = mutableListOf<TaskInfoCollector>()

    fun addTaskInfoCollector(taskInfoCollector: TaskInfoCollector) = taskInfoCollectors.add(taskInfoCollector)

    override fun collect() = taskInfoCollectors.map(TaskInfoCollector::collect)
}