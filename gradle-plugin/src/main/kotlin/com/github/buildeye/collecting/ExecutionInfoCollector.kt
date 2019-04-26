package com.github.buildeye.collecting

import com.github.buildeye.infos.ExecutionInfo
import java.util.*
import kotlin.properties.Delegates

class ExecutionInfoCollector : Collector<ExecutionInfo> {
    lateinit var startedDate: Date
    var startedTimestamp: Long by Delegates.notNull()
    val taskInfosCollector = TaskInfosCollector()

    override fun collect() = ExecutionInfo(
            startedDate,
            taskInfosCollector.collect().map {
                it.copy(
                        startedTimestamp = it.startedTimestamp - this.startedTimestamp,
                        finishedTimestamp = it.finishedTimestamp - this.startedTimestamp
                )
            }
    )
}