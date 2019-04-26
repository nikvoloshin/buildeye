package com.github.buildeye.collecting.collectors

import com.github.buildeye.infos.InfrastructureInfo
import org.gradle.StartParameter
import java.nio.charset.Charset
import java.util.*

class InfrastructureInfoCollector(private val startParameter: StartParameter) : Collector<InfrastructureInfo> {
    override fun collect() = InfrastructureInfo(
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
}