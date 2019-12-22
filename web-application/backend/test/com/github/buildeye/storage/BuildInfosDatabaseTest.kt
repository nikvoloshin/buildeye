package com.github.buildeye.storage

import com.github.buildeye.infos.*
import org.jetbrains.squash.dialects.h2.H2Connection
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull

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
    fun fetchBuildInfo(){
        val id = storage.insertBuildInfo(buildInfo)
        assertEquals(buildInfo, storage.fetchBuildInfo(id).buildInfo)
    }

    @Test
    fun fetchBuildInfoNull(){
        assertFailsWith<NoSuchElementException> {
            assertEquals(buildInfo, storage.fetchBuildInfo(0).buildInfo)
        }
    }

    @Test
    fun fetchAllBuildInfosEmpty(){
        assertFalse(storage.fetchAllBuildInfos().isNotEmpty())
    }

    @Test
    fun fetchAllBuildInfos(){
        val id1 = storage.insertBuildInfo(buildInfo)
        val id2 = storage.insertBuildInfo(buildInfo)
        val id3 = storage.insertBuildInfo(buildInfo)
        val id4 = storage.insertBuildInfo(buildInfo)
        assertEquals(4, ArrayList(storage.fetchAllBuildInfos()).size)

        assertEquals(listOf(id1, id2, id3, id4), storage.fetchAllBuildInfos().map(IndexedBuildInfo::id))

        val indexedBuildInfo1 = storage.fetchAllBuildInfos().last()
        assertEquals(Pair(id4, buildInfo), Pair(indexedBuildInfo1.id, indexedBuildInfo1.buildInfo))
    }

    @Test
    fun close() {
        (storage as BuildInfosDatabase).close()
    }
}