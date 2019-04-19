package com.github.buildeye.collecting

import com.github.buildeye.collecting.property.InfoCollector
import com.github.buildeye.collecting.property.InfoProvider
import com.github.buildeye.infos.ExecutionInfo
import com.github.buildeye.property.EitherValueFailure
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value
import java.util.*

class ExecutionInfoCollector : InfoCollector<ExecutionInfo>("execution info") {
    private val startedDateProvider = InfoProvider<Date>("execution started date")
    private val startedTimestampProvider = InfoProvider<Long>("execution started timestamp")
    private val taskInfosCollector = TaskInfosCollector()

    override fun get(): EitherValueFailure<ExecutionInfo, List<String>> {
        val startedDate = startedDateProvider.get()
        val startedTimestamp = startedTimestampProvider.get()
        val taskInfos = taskInfosCollector.get()

        val failures = mutableListOf<String>()
        if (startedDate is Failure) {
            failures.add(startedDate.getFailure())
        }
        if (startedTimestamp is Failure) {
            failures.add(startedTimestamp.getFailure())
        }
        if (taskInfos is Failure) {
            failures.addAll(taskInfos.getFailure())
        }

        return if (failures.isNotEmpty()) {
            Failure(failures)
        } else {
            Value(
                    ExecutionInfo(
                            (startedDate as Value).get(),
                            (startedTimestamp as Value).get(),
                            (taskInfos as Value).get()
                    )
            )
        }
    }

    fun setStartedDate(date: Date) = startedDateProvider.set(date)

    fun setStartedTimestamp(startedTimestamp: Long) = startedTimestampProvider.set(startedTimestamp)

    fun getTaskInfosCollector() = taskInfosCollector
}