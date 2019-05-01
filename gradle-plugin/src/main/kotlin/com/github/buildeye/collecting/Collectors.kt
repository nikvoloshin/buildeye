package com.github.buildeye.collecting

import com.github.buildeye.infos.*
import org.gradle.BuildResult
import org.gradle.StartParameter
import org.gradle.api.tasks.TaskState
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

object Collectors {
    fun switchesInfo(startParameter: StartParameter) = with(startParameter) {
        SwitchesInfo(
                isBuildCacheEnabled,
                isConfigureOnDemand,
                isContinueOnFailure,
                isContinuous,
                isDryRun,
                isOffline,
                isParallelProjectExecutionEnabled,
                isRerunTasks,
                isRefreshDependencies
        )
    }

    fun infrastructureInfo(startParameter: StartParameter) = InfrastructureInfo(
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            Runtime.getRuntime().availableProcessors(),
            startParameter.maxWorkerCount,
            System.getProperty("java.runtime.name"),
            System.getProperty("java.runtime.version"),
            System.getProperty("java.vm.name"),
            System.getProperty("java.vm.vendor"),
            System.getProperty("java.vm.version"),
            Runtime.getRuntime().maxMemory() / (1024 * 1024),
            Locale.getDefault().displayName,
            Charset.defaultCharset().displayName()
    )

    fun buildInfo(buildData: BuildData) = with(buildData) {
        BuildInfo(
                switchesInfo,
                infrastructureInfo,
                executionInfo(executionData),
                buildResultInfo
        )
    }

    private fun executionInfo(executionData: ExecutionData) = with(executionData) {
        ExecutionInfo(
                startedDate,
                getAllTasksData().map(::taskInfo)
        )
    }

    private fun taskInfo(taskData: TaskData) = with(taskData) {
        TaskInfo(
                path,
                startedOffset,
                stopwatch.elapsed(TimeUnit.MILLISECONDS),
                stateInfo
        )
    }

    fun taskStateInfo(taskState: TaskState, outOfDateReason: OutOfDateReason? = null) = with(taskState) {
        TaskStateInfo(
                executed,
                failureInfo(failure),
                didWork,
                skipped,
                skipMessage,
                upToDate,
                outOfDateReason,
                noSource
        )
    }

    fun buildResultInfo(buildResult: BuildResult) = BuildResultInfo(
            BuildResultInfo.Action.valueOf(buildResult.action.toUpperCase()),
            failureInfo(buildResult.failure)
    )

    private fun failureInfo(failure: Throwable?) = failure?.let {
        FailureInfo(
                failure.message ?: "",
                failure.cause?.toString() ?: ""
        )
    }
}
