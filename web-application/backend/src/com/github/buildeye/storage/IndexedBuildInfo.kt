package com.github.buildeye.storage

import com.github.buildeye.infos.BuildInfo

data class IndexedBuildInfo(val id: Int, val buildInfo: BuildInfo?)