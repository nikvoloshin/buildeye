package com.github.buildeye.storage

import com.github.buildeye.infos.*
import org.jetbrains.squash.dialects.h2.H2Connection
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class BuildInfosDatabaseTest {
    private val storage: BuildInfosStorage = BuildInfosDatabase(H2Connection.createMemoryConnection())
    private val project = "test"
    private val switchesInfo = SwitchesInfo(
            isBuildCacheEnabled = true,
            isConfigureOnDemand = true,
            isContinueOnFailure = true,
            isContinuous = true,
            isDryRun = true,
            isOffline = true,
            isParallelProjectExecutionEnabled = true,
            isRerunTasks = true,
            isRefreshDependencies = true
    )
    private val infrastructureInfo = InfrastructureInfo(
            "linux",
            "19.04",
            4,
            4,
            "test",
            "test",
            "test",
            "test",
            "test",
            256L,
            "test",
            "test"
    )
    private val executionInfo = ExecutionInfo(12L, 12L, Collections.emptyList())
    private val buildResultInfo = BuildResultInfo(BuildResultInfo.Action.BUILD, failure = FailureInfo("none", "none"))
    private val buildInfo = BuildInfo(project, switchesInfo, infrastructureInfo, executionInfo, buildResultInfo)

    @Test
    fun test(){
        storage.insertBuildInfo(buildInfo)
        assertEquals(buildInfo, storage.fetchBuildInfo(0).buildInfo)
    }

    @Test
    fun close() {
    }
}