package com.github.buildeye.storage

import com.github.buildeye.infos.*
import org.jetbrains.squash.dialects.h2.H2Connection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class BuildInfosDatabaseTest {
    private lateinit var storage: BuildInfosDatabase

    @BeforeEach
    fun setUp() {
        storage = BuildInfosDatabase(H2Connection.createMemoryConnection())
    }

    @AfterEach
    fun tearDown() {
        storage.close()
    }

    @Test
    fun fetchBuildInfo() {
        val id = storage.insertBuildInfo(buildInfo)
        assertEquals(buildInfo, storage.fetchBuildInfo(id).buildInfo)
    }

    @Test
    fun fetchBuildInfoNull() {
        assertFailsWith<NoSuchElementException> { storage.fetchBuildInfo(0).buildInfo }
    }

    @Test
    fun fetchAllBuildInfosEmpty() {
        assertTrue(storage.fetchAllBuildInfos().isEmpty())
    }

    @Test
    fun fetchAllBuildInfos() {
        val id1 = storage.insertBuildInfo(buildInfo)
        val id2 = storage.insertBuildInfo(buildInfo)
        val id3 = storage.insertBuildInfo(buildInfo)
        val id4 = storage.insertBuildInfo(buildInfo)
        assertEquals(4, storage.fetchAllBuildInfos().size)

        assertEquals(listOf(id1, id2, id3, id4), storage.fetchAllBuildInfos().map { it.id })

        val buildInfo = storage.fetchAllBuildInfos().last()
        assertEquals(id4 to this.buildInfo, buildInfo.id to buildInfo.buildInfo)
    }

    @Test
    fun close() = storage.close()

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

}