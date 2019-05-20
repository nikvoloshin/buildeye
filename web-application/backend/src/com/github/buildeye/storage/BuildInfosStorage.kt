package com.github.buildeye.storage

import com.github.buildeye.infos.BuildInfo

interface BuildInfosStorage {
    fun fetchBuildInfo(id: Int): IndexedBuildInfo
    fun fetchAllBuildInfos(): List<IndexedBuildInfo>
    fun insertBuildInfo(buildInfo: BuildInfo): Int
}