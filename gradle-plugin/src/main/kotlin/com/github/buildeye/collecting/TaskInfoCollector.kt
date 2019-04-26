package com.github.buildeye.collecting

import com.github.buildeye.infos.TaskInfo
import com.github.buildeye.infos.TaskStateInfo
import kotlin.properties.Delegates

class TaskInfoCollector : Collector<TaskInfo> {
    lateinit var name: String
    lateinit var path: String
    var startedTimestamp: Long by Delegates.notNull()
    var finishedTimestamp: Long by Delegates.notNull()
    lateinit var taskStateInfo: TaskStateInfo

    override fun collect() = TaskInfo(
            name,
            path,
            startedTimestamp,
            finishedTimestamp,
            taskStateInfo
    )
}