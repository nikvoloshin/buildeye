package com.github.buildeye.collecting

import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.InfrastructureInfo
import com.github.buildeye.infos.SwitchesInfo
import com.github.buildeye.infos.TaskStateInfo
import com.github.buildeye.time.SplitStopwatch
import org.gradle.internal.impldep.com.google.common.base.Stopwatch
import kotlin.properties.Delegates

class BuildData {
    lateinit var switchesInfo: SwitchesInfo
    lateinit var infrastructureInfo: InfrastructureInfo
    val executionData = ExecutionData()
    lateinit var buildResultInfo: BuildResultInfo
}

class ExecutionData {
    var startedDate: Long by Delegates.notNull()
    private val tasksData = mutableMapOf<String, TaskData>()
    val stopwatch = SplitStopwatch()

    @Synchronized fun getTaskData(path: String) =
            tasksData[path] ?: TaskData().also { tasksData[path] = it }

    @Synchronized fun getAllTasksData() = tasksData.values.toList()
}

class TaskData {
    lateinit var path: String
    var startedOffset: Long by Delegates.notNull()
    val stopwatch: Stopwatch = Stopwatch.createUnstarted()
    lateinit var stateInfo: TaskStateInfo
}
