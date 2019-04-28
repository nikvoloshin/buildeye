package com.github.buildeye.collecting

import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.InfrastructureInfo
import com.github.buildeye.infos.SwitchesInfo
import com.github.buildeye.infos.TaskStateInfo
import java.util.*
import kotlin.properties.Delegates

class BuildData {
    lateinit var switchesInfo: SwitchesInfo
    lateinit var infrastructureInfo: InfrastructureInfo
    val executionData = ExecutionData()
    lateinit var buildResultInfo: BuildResultInfo
}

class ExecutionData {
    lateinit var startedDate: Date
    var startedTimeStamp: Long by Delegates.notNull()
    private val tasksData = mutableMapOf<String, TaskData>()

    fun getTaskData(path: String) =
            tasksData[path] ?: TaskData().also { tasksData[path] = it }

    fun getAllTasksData() = tasksData.values.toList()
}

class TaskData {
    lateinit var name: String
    lateinit var path: String
    var startedTimeStamp: Long by Delegates.notNull()
    var finishedTimeStamp: Long by Delegates.notNull()
    lateinit var stateInfo: TaskStateInfo
}
