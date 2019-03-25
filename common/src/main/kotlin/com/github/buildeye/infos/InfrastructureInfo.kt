package com.github.buildeye.infos

data class InfrastructureInfo(
        val osName: String?,
        val osVersion: String?,
        val cpuCores: Int,
        val maxGradleWorkers: Int,
        val jreName: String?,
        val jreVersion: String?,
        val vmName: String?,
        val vmVendor: String?,
        val vmVersion: String?,
        val maxVMHeapSize: Long,
        val locale: String,
        val defaultCharset: String
)