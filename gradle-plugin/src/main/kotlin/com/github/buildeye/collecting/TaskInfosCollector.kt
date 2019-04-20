package com.github.buildeye.collecting

import com.github.buildeye.infos.TaskInfo

class TaskInfosCollector : Collector<List<TaskInfo>> {
    private val taskInfos = mutableListOf<TaskInfo>()

    fun addTaskInfo(taskInfo: TaskInfo) = taskInfos.add(taskInfo)

    override fun collect() = taskInfos
}